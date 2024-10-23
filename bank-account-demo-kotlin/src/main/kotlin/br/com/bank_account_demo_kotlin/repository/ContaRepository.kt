package br.com.bank_account_demo_kotlin.repository

import br.com.bank_account_demo_kotlin.model.Conta
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ContaRepository : MongoRepository<Conta, String> {

    fun findByDocument(document: String): Optional<Conta>
}