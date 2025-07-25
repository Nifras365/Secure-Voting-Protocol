package server;

import crypto.CryptoUtils;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

public class TallyingAuthority {
    private final KeyPair keyPair;

    public TallyingAuthority() throws NoSuchAlgorithmException {
        this.keyPair = CryptoUtils.generateRsaKeyPair();
        System.out.println("🟢 Tallying Authority is ready. It has a secret key.");
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public void tallyVotes(List<byte[]> bulletinBoard) throws Exception {
        System.out.println("\n\n==============================================================");
        System.out.println("STEP 5: ELECTION IS OVER! COUNTING THE VOTES");
        System.out.println("==============================================================");
        Map<String, Integer> results = new HashMap<>();

        int voteNumber = 1;
        for (byte[] encryptedVote : bulletinBoard) {
            String decrypted = CryptoUtils.rsaDecrypt(encryptedVote, keyPair.getPrivate());
            String[] parts = decrypted.split("\\|\\|");
            String choice = parts[0];
            String hash = parts[1];

            if (CryptoUtils.hash(choice).equals(hash)) {
                results.put(choice, results.getOrDefault(choice, 0) + 1);
            } else {
                System.out.println("🔴 Hash mismatch! Vote discarded.");
            }
            voteNumber++;
        }

        System.out.println("\n************************");
        System.out.println("* FINAL ELECTION RESULTS *");
        System.out.println("************************");
        results.forEach((candidate, count) ->
                System.out.println("Candidate '" + candidate + "': " + count + " votes")
        );
    }
}
