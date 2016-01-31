package com.example.wenda.tarucnfc.Databases.Contracts;


import android.provider.BaseColumns;

public class TransactionContract {

    public TransactionContract() {
    }

    public static abstract class TransactionRecord implements BaseColumns {
        public static final String CLASS_SCHEDULE_TABLE = "TransactionHistory";
        public static final String COLUMN_SENDER_ID = "SenderID";
        public static final String COLUMN_RECIPIENT_ID = "RecipientID";
        public static final String COLUMN_TRANSACTION_TYPE = "TransactionType";
        public static final String COLUMN_Amount = "Amount";
        public static final String COLUMN_DATE_TIME = "DateTime";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_REMARK = "Remark";
    }
}
