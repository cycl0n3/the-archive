package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class ClassicModelsRepository {

    private static final Logger log = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;
    private final TransactionTemplate template;

    public ClassicModelsRepository(DSLContext ctx, TransactionTemplate template) {
        this.ctx = ctx;
        this.template = template;
    }

    public void fetchProductsViaTwoTransactions() throws InterruptedException {
        Thread tA = new Thread(() -> {
            template.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);

            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    log.info("Starting first transaction (A) ...");

                    SaleRecord sr = ctx.selectFrom(SALE)
                            .where(SALE.SALE_ID.eq(2L))
                            .forNoKeyUpdate()
                            .fetchSingle();

                    try {
                        log.info("Holding in place first transaction (A) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    ctx.insertInto(TOKEN)
                            .set(TOKEN.SALE_ID, sr.getSaleId())
                            .set(TOKEN.AMOUNT, 1200.5)
                            .execute();
                }
            });

            log.info("First transaction (A) committed!");
        });

        Thread tB = new Thread(() -> {
            template.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);

            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    log.info("Starting second transaction (B) ...");
                    log.info("Second transaction (B) can aquire a FOR KEY SHARE lock ...");
                    ctx.selectFrom(SALE)
                            .where(SALE.SALE_ID.eq(2L))
                            .forKeyShare()
                            .fetchSingle();
                    
                    log.info("Second transaction (B) has acquired FOR KEY SHARE lock ...");
                }
            });

            log.info("Second transaction (B) committed!");
        });

        Thread tC = new Thread(() -> {
            template.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);

            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    log.info("Starting third transaction (C) ...");
                    log.info("Third transaction (C) cannot aquire a FOR SHARE lock until transaction (A) releases its lock ...");
                    ctx.selectFrom(SALE)
                            .where(SALE.SALE_ID.eq(2L))
                            .forShare()
                            .fetchSingle();
                    
                    log.info("Third transaction (C) has acquired FOR SHARE lock ...");
                }
            });

            log.info("Third transaction (C) committed!");
        });

        tA.start();
        Thread.sleep(2500);
        tB.start();
        Thread.sleep(2500);
        tC.start();

        tA.join();
        tB.join();
        tC.join();
    }
}
