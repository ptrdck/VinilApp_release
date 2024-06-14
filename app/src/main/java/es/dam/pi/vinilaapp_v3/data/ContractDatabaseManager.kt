package es.dam.pi.vinilaapp_v3.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import es.dam.pi.vinilaapp_v3.ui.model.Contract
import kotlinx.coroutines.tasks.await

class ContractDatabaseManager {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    suspend fun createContract(contract: Contract): Boolean {
        val userId = contract.userId
        val contractCount = getContractsCountByUserId(userId)
        if (contractCount >= 3) {
            return false // No se puede crear más de 3 contratos
        }
        val contractId = database.child("contracts").child(userId).push().key ?: return false
        val contractWithId = contract.copy(id = contractId)
        database.child("contracts").child(userId).child(contractId).setValue(contractWithId).await()
        return true
    }

    suspend fun updateContract(contract: Contract) {
        database.child("contracts").child(contract.userId).child(contract.id).setValue(contract).await()
    }

    suspend fun deleteContract(contractId: String, userId: String) {
        database.child("contracts").child(userId).child(contractId).removeValue().await()
    }

    suspend fun getContractsByUserId(userId: String): List<Contract> {
        val snapshot = database.child("contracts").child(userId).get().await()
        return snapshot.children.mapNotNull { it.getValue(Contract::class.java) }
    }

    private suspend fun getContractsCountByUserId(userId: String): Int {
        val snapshot = database.child("contracts").child(userId).get().await()
        return snapshot.childrenCount.toInt()
    }
}
