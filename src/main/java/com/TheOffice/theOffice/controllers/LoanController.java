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

    public LoanController(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createLoan(@RequestBody Map<String, Object> request) {
        BigDecimal loanAmount = new BigDecimal(request.get("loan_amount").toString());
        BigDecimal interestRate = new BigDecimal(request.get("interest_rate").toString());
        BigDecimal rest = new BigDecimal(request.get("rest").toString());
        Integer duration = (Integer) request.get("duration");
        Long id_user = ((Number) request.get("id_user")).longValue();

        int id_loan = loanDao.save(loanAmount, interestRate, duration, rest, id_user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_loan", id_loan,
                "loan_amount", loanAmount,
                "interest_rate", interestRate,
                "duration", duration,
                "rest", rest,
                "id_user", id_user
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        Loan updatedLoan = loanDao.update(id, loan);
        return ResponseEntity.ok(updatedLoan);
    }
}
