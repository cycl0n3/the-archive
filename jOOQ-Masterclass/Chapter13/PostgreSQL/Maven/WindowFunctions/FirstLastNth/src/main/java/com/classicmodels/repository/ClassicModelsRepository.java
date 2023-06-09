package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.lastValue;
import static org.jooq.impl.DSL.nthValue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  - FIRST_VALUE() returns the value of the specified expression with 
          respect to the first row in the window frame.
    
        - NTH_VALUE() returns value of argument from Nth row of the window frame.
    
        - LAST_VALUE() returns the value of the specified expression with 
          respect to the last row in the window frame. */
    
    public void cheapestAndMostExpensiveProduct() {

        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                firstValue(PRODUCT.PRODUCT_NAME).over().orderBy(PRODUCT.BUY_PRICE).as("cheapest"),
                lastValue(PRODUCT.PRODUCT_NAME).over().orderBy(PRODUCT.BUY_PRICE)
                        .rangeBetweenUnboundedPreceding().andUnboundedFollowing().as("most_expensive"))
                .from(PRODUCT)
                .fetch();
    }

    public void cheapestAndMostExpensiveProductByProductLine() {

        ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                firstValue(PRODUCT.PRODUCT_NAME).over()
                        .partitionBy(PRODUCT.PRODUCT_LINE).orderBy(PRODUCT.BUY_PRICE).as("cheapest"),
                lastValue(PRODUCT.PRODUCT_NAME).over()
                        .partitionBy(PRODUCT.PRODUCT_LINE).orderBy(PRODUCT.BUY_PRICE)
                        .rangeBetweenUnboundedPreceding().andUnboundedFollowing().as("most_expensive"))
                .from(PRODUCT)
                .fetch();
    }

    public void saleOfAnImmediatelyBetterSalePerFiscalYear() {
       
        ctx.select(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_,
                firstValue(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.SALE_)
                        .groupsBetweenFollowing(1).andFollowing(1).as("sale_of_better"))
                .from(SALE)
                .orderBy(SALE.FISCAL_YEAR, SALE.SALE_)
                .fetch();

    }

    public void secondCheapestProduct() {

        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                nthValue(PRODUCT.PRODUCT_NAME, 2)
                        .over().orderBy(PRODUCT.BUY_PRICE).as("second_cheapest"))
                .from(PRODUCT)
                .fetch();
    }

    public void secondMostExpensiveProductByProductLine() {

        // PostgreSQL doesn't support fromLast() so we order desc        
        ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                nthValue(PRODUCT.PRODUCT_NAME, 2).over()
                        .partitionBy(PRODUCT.PRODUCT_LINE).orderBy(PRODUCT.BUY_PRICE.desc())
                        .rangeBetweenUnboundedPreceding().andUnboundedFollowing().as("second_most_expensive"))
                .from(PRODUCT)
                .fetch();
    }
}
