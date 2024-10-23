package br.com.bank_account_demo_kotlin.controller

import br.com.bank_account_demo_kotlin.model.Conta
import br.com.bank_account_demo_kotlin.repository.ContaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("contas")
class ContaController (val repository: ContaRepository) {

    /**
     * Com familiaridade com Java

    @PostMapping
    fun create(@RequestBody conta: Conta) : ResponseEntity<Conta>{
        val contaCriada = repository.save(conta);
        return ResponseEntity.ok(contaCriada);
    }
    */

    @PostMapping
    fun create(@RequestBody conta: Conta) : ResponseEntity<Conta> = ResponseEntity.ok(repository.save(conta))

    @GetMapping
    fun read() = ResponseEntity.ok(repository.findAll())

    @PutMapping("{documento}")
    fun update(@PathVariable documento: String, @RequestBody conta: Conta):ResponseEntity<Conta>{
        val contaOptional = repository.findByDocument(documento);
        val contaParaSalvar = contaOptional.orElseThrow {RuntimeException("Conta não identificada pelo documento $documento!")}
        return ResponseEntity.ok(repository.save(contaParaSalvar.copy(name=conta.name, saldo = conta.saldo)));
    }

    @DeleteMapping("{documento}")
    fun delete(@PathVariable documento: String) = repository.findByDocument(documento)
            .ifPresent{repository.delete(it)}

}