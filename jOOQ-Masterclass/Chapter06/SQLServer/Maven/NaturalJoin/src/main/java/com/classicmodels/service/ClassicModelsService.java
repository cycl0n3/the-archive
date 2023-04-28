package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {
        
        classicModelsRepository.naturalJoinEmployeeSale();                  // EXAMPLE 1
        classicModelsRepository.naturalLeftOuterJoinEmployeeSale();         // EXAMPLE 2        
        classicModelsRepository.naturalRightOuterJoinEmployeeSale();        // EXAMPLE 3
        classicModelsRepository.naturalFullOuterJoinEmployeeSale();         // EXAMPLE 4
        classicModelsRepository.naturalJoinOrderCustomerPayment();          // EXAMPLE 5
        classicModelsRepository.naturalJoinOfficeCustomerdetail();          // EXAMPLE 6
        classicModelsRepository.naturalJoinPaymentBankTransaction();        // EXAMPLE 7        
    }
}