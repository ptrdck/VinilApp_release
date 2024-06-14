package es.dam.pi.vinilaapp_v3.viewmodel.provider

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.dam.pi.vinilaapp_v3.data.ContractDatabaseManager
import es.dam.pi.vinilaapp_v3.viewmodel.ContractViewModel

class ContractViewModelFactory(
    private val contractDatabaseManager: ContractDatabaseManager,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractViewModel(contractDatabaseManager, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
