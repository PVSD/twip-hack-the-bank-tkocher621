package com.company;

/**
 * Created by dpennebacker on 2/13/17.
 */
public class bankAccount implements Comparable {

    public bankAccount(String nm, double amt) {
        name = nm;
        balance = amt;
    }

    public int compareTo(Object otherObject) {
        bankAccount otherAccount = (bankAccount) otherObject;
        int retValue;
        if (balance < otherAccount.balance) {
            retValue = -1;
        } else {
            if (balance > otherAccount.balance) {
                retValue = 1;
            } else {
                retValue = 0;
            }
        }
        return retValue;
    }

    public String name;
    public double balance;

}
