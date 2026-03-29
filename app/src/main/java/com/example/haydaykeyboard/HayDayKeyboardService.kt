package com.example.haydaykeyboard

import android.inputmethodservice.InputMethodService
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import com.example.haydaykeyboard.data.HayDayRepository
import com.example.haydaykeyboard.data.Machine
import com.example.haydaykeyboard.data.Product
import com.example.haydaykeyboard.databinding.KeyboardViewBinding
import com.example.haydaykeyboard.databinding.ItemMachineTabBinding
import com.example.haydaykeyboard.ui.ProductAdapter
import com.example.haydaykeyboard.util.SettingsStore
import com.google.android.material.button.MaterialButton

class HayDayKeyboardService : InputMethodService() {

    private lateinit var binding: KeyboardViewBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var settings: SettingsStore
    private lateinit var repository: HayDayRepository

    private val machines: List<Machine> get() = repository.machines
    private val products: List<Product> get() = repository.products

    private var selectedMachineId: String? = null
    private var favoritesOnly = false
    private var minLevel = 1
    private var maxLevel = 150
    private var query = ""
    private var favorites: MutableSet<String> = mutableSetOf()

    override fun onCreateInputView(): View {
        binding = KeyboardViewBinding.inflate(LayoutInflater.from(this))
        settings = SettingsStore(this)
        repository = HayDayRepository.get(this)
        favorites = settings.favorites().toMutableSet()
        selectedMachineId = settings.lastMachineId()

        adapter = ProductAdapter(
            isArabic = { settings.isArabic() },
            isFavorite = { favorites.contains(it) },
            onFavoriteClick = ::toggleFavorite,
            onProductClick = ::replaceWholeFieldWithProduct,
        )

        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = adapter
        binding.btnBackspace.setOnClickListener { currentInputConnection?.deleteSurroundingText(1, 0) }
        binding.btnLanguage.setOnClickListener { toggleLanguage() }
        binding.btnAll.setOnClickListener { favoritesOnly = false; updateModeButtons(); applyFilters() }
        binding.btnFavorites.setOnClickListener { favoritesOnly = true; updateModeButtons(); applyFilters() }
        binding.btnResetFilters.setOnClickListener { resetFilters() }
        binding.levelSlider.valueFrom = 1f
        binding.levelSlider.valueTo = 150f
        binding.levelSlider.setValues(1f, 150f)
        binding.levelSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values.sorted()
            minLevel = values.first().toInt()
            maxLevel = values.last().toInt()
            updateLevelLabel()
            applyFilters()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                query = s?.toString().orEmpty().trim()
                applyFilters()
            }
        })

        buildMachineTabs()
        updateModeButtons()
        updateLevelLabel()
        updateDatasetMeta()
        applyFilters()
        return binding.root
    }

    private fun resetFilters() {
        selectedMachineId = null
        favoritesOnly = false
        minLevel = 1
        maxLevel = 150
        query = ""
        binding.etSearch.setText("")
        binding.levelSlider.setValues(1f, 150f)
        buildMachineTabs()
        updateModeButtons()
        updateLevelLabel()
        applyFilters()
    }

    private fun buildMachineTabs() {
        binding.machineTabsContainer.removeAllViews()
        addMachineTab(null, getString(R.string.machine_all))
        machines.forEach { machine ->
            addMachineTab(machine.id, if (settings.isArabic()) machine.nameAr else machine.nameEn)
        }
        refreshMachineTabs()
    }

    private fun addMachineTab(machineId: String?, title: String) {
        val chipBinding = ItemMachineTabBinding.inflate(LayoutInflater.from(this), binding.machineTabsContainer, false)
        chipBinding.root.text = title
        chipBinding.root.setOnClickListener {
            selectedMachineId = machineId
            settings.setLastMachineId(machineId)
            refreshMachineTabs()
            applyFilters()
        }
        binding.machineTabsContainer.addView(chipBinding.root)
    }

    private fun refreshMachineTabs() {
        binding.machineTabsContainer.children.forEachIndexed { index, view ->
            if (view is MaterialButton) {
                val selected = if (index == 0) selectedMachineId == null else machines.getOrNull(index - 1)?.id == selectedMachineId
                view.isChecked = selected
                view.setBackgroundResource(if (selected) R.drawable.bg_chip_selected else R.drawable.bg_chip)
                view.alpha = if (selected) 1f else 0.9f
            }
        }
    }

    private fun updateModeButtons() {
        binding.btnAll.setBackgroundResource(if (!favoritesOnly) R.drawable.bg_chip_selected else R.drawable.bg_chip)
        binding.btnFavorites.setBackgroundResource(if (favoritesOnly) R.drawable.bg_chip_selected else R.drawable.bg_chip)
    }

    private fun updateLevelLabel() {
        binding.tvLevelRange.text = "$minLevel - $maxLevel"
    }

    private fun updateDatasetMeta() {
        binding.tvDatasetMeta.text = getString(R.string.dataset_meta, products.size, machines.size)
    }

    private fun toggleLanguage() {
        settings.setArabic(!settings.isArabic())
        buildMachineTabs()
        updateDatasetMeta()
        applyFilters()
    }

    private fun toggleFavorite(product: Product) {
        if (!favorites.add(product.id)) favorites.remove(product.id)
        settings.setFavorites(favorites)
        applyFilters()
    }

    private fun applyFilters() {
        val filtered = products
            .asSequence()
            .filter { selectedMachineId == null || it.machineId == selectedMachineId }
            .filter { it.level in minLevel..maxLevel }
            .filter { !favoritesOnly || favorites.contains(it.id) }
            .filter { product ->
                if (query.isBlank()) return@filter true
                val q = query.lowercase()
                listOf(product.nameEn, product.nameAr, product.machineNameEn, product.machineNameAr)
                    .any { it.lowercase().contains(q) }
            }
            .sortedWith(compareBy<Product> { it.level }.thenBy { if (settings.isArabic()) it.nameAr else it.nameEn })
            .toList()

        adapter.submitList(filtered)
        val machineTitle = machines.firstOrNull { it.id == selectedMachineId }?.let { if (settings.isArabic()) it.nameAr else it.nameEn }
            ?: getString(R.string.machine_all)
        binding.tvHeader.text = getString(R.string.header_results, machineTitle, filtered.size)
        binding.tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.rvProducts.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun replaceWholeFieldWithProduct(product: Product) {
        val inputConnection = currentInputConnection ?: return
        val extracted = inputConnection.getExtractedText(ExtractedTextRequest(), 0)
        val fullText = extracted?.text?.toString().orEmpty()
        val selectionStart = extracted?.selectionStart ?: fullText.length
        val selectionEnd = extracted?.selectionEnd ?: fullText.length
        val insertText = if (settings.isArabic()) product.nameAr else product.nameEn

        inputConnection.beginBatchEdit()
        if (fullText.isNotEmpty()) {
            val beforeCursor = selectionStart.coerceIn(0, fullText.length)
            val afterCursor = (fullText.length - selectionEnd.coerceIn(0, fullText.length)).coerceAtLeast(0)
            inputConnection.deleteSurroundingText(beforeCursor, afterCursor)
        } else {
            inputConnection.deleteSurroundingText(Int.MAX_VALUE, Int.MAX_VALUE)
        }
        inputConnection.commitText(insertText, 1)
        inputConnection.endBatchEdit()
    }
}
