package com.wallee.android.sdk;

/**
 * Created by simonwalter on 13.07.17.
 */
public class TransactionCredentialsTest {

    private char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
////*
//    public String createDummyCredentials(int spaceId, int transactionId, Date time) {
//        String seperator = "-";
//        String end = "-grantedUser419";
//        return spaceId + seperator + transactionId + seperator + time.getTime() + seperator + randomString(CHARSET_AZ_09, 44) + seperator + end;
//
//    }
//
//    public String randomString(char[] characterSet, int length) {
//        Random random = new SecureRandom();
//        char[] result = new char[length];
//        for (int i = 0; i < result.length; i++) {
//            // picks a random index out of character set > random character
//            int randomCharIndex = random.nextInt(characterSet.length);
//            result[i] = characterSet[randomCharIndex];
//        }
//        return new String(result);
//    }
//
//    @Test
//    public void validCredentialsTest1() throws Exception {
//        /* Test with valid credentials. This calls to getCredentials()
//        *   should not throw errors.
//        */
//        Calendar cal1 = Calendar.getInstance();
//        String dummy1 = createDummyCredentials(1, 2, cal1.getTime());
//        String creds1 = new TransactionData(dummy1, 2).getCredentials();
//    }
//
//    @Test
//    public void validCredentialsTest2() throws Exception {
//        /* Test with valid credentials. This calls to getCredentials()
//        *   should not throw errors.
//        */
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.MINUTE, -10);
//        String dummy1 = createDummyCredentials(1, 2, cal1.getTime());
//        String creds1 = new TransactionData(dummy1, 2).getCredentials();
//    }
//
//    @Test
//    public void validCredentialsTest3() throws Exception {
//        /* Test with valid credentials. This calls to getCredentials()
//        *   should not throw errors.
//        */
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.MINUTE, -14);
//        cal1.add(Calendar.SECOND, -59);
//        String dummy1 = createDummyCredentials(1, 2, cal1.getTime());
//        String creds1 = new TransactionData(dummy1, 2).getCredentials();
//    }
//
//
//    @Test
//    public void invalidCredentialsTest1() throws Exception {
//
//        //Calendar set to the current date
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MINUTE, -90);
//        String credentials = createDummyCredentials(1, 2, cal.getTime());
//        testForInvalidity(credentials);
//
//    }
//
//    @Test
//    public void invalidCredentialsTest2() throws Exception {
//
//        //Calendar set to the current date
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.MINUTE, -15);
//        String credentials = createDummyCredentials(1, 2, cal1.getTime());
//        testForInvalidity(credentials);
//
//    }
//
//    @Test
//    public void invalidCredentialsTest3() throws Exception {
//
//        //Calendar set to the current date
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.YEAR, -100);
//        String credentials = createDummyCredentials(1, 2, cal1.getTime());
//        testForInvalidity(credentials);
//
//    }
//
//    @Test
//    public void invalidCredentialsTest4() throws Exception {
//
//        //Calendar set to the current date
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.MINUTE, 1);
//        String credentials = createDummyCredentials(1, 2, cal1.getTime());
//        testForInvalidity(credentials);
//
//    }
//
//    @Test
//    public void invalidCredentialsTest5() throws Exception {
//
//        //Calendar set to the current date
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.SECOND, 1);
//        String credentials = createDummyCredentials(1, 2, cal1.getTime());
//        testForInvalidity(credentials);
//
//    }
//
//    public void testForInvalidity(String credentials) throws Exception{
//
//        boolean isValid = true;
//        try {
//            TransactionData transaction = new TransactionData(credentials, 2);
//            String creds = transaction.getCredentials();
//        } catch (InvalidCredentialsException e) {
//            isValid = false;
//        }
//        if(isValid) {
//            throw new Exception("Credentials should not be valid");
//        }
//    }
}
