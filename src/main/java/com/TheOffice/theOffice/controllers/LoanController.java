package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.LoanDao;
import com.TheOffice.theOffice.entities.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanDao loanDao;

    // Constructeur avec injection de dépendance pour le DAO
    public LoanController(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    // Récupérer tous les prêts
    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanDao.findAll());  // Renvoie tous les prêts dans une réponse HTTP OK
    }

    // Récupérer un prêt par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanDao.findById(id));  // Renvoie le prêt correspondant à l'ID
    }

    // Créer un prêt
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createLoan(@RequestBody Map<String, Object> request) {
        // Extraction des informations envoyées dans la requête
        BigDecimal loanAmount = new BigDecimal(request.get("loan_amount").toString());
        BigDecimal interestRate = new BigDecimal(request.get("interest_rate").toString());
        BigDecimal rest = new BigDecimal(request.get("rest").toString());
        Integer duration = (Integer) request.get("duration");
        Long id_user = ((Number) request.get("id_user")).longValue();

        // Sauvegarde du prêt et récupération de son ID
        int id_loan = loanDao.save(loanAmount, interestRate, duration, rest, id_user);

        // Construction de la réponse HTTP avec statut 201 (créé) et données du prêt
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_loan", id_loan,
                "loan_amount", loanAmount,
                "interest_rate", interestRate,
                "duration", duration,
                "rest", rest,
                "id_user", id_user
        ));
    }

    // Mettre à jour un prêt existant
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        // Mise à jour du prêt
        Loan updatedLoan = loanDao.update(id, loan);
        return ResponseEntity.ok(updatedLoan);  // Renvoie le prêt mis à jour
    }
}
