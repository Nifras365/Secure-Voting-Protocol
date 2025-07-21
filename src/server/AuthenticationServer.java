package server;

import crypto.CryptoUtils;
import model.Voter;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

public class AuthenticationServer {
    private final KeyPair keyPair;
    private final List<String> eligibleVoters;
    private final Set<String> votersWhoReceivedToken = new HashSet<>();

    public AuthenticationServer(List<String> eligibleVoters) throws NoSuchAlgorithmException {
        this.keyPair = CryptoUtils.generateRsaKeyPair();
        this.eligibleVoters = eligibleVoters;
        System.out.println("✅ Authentication Server is ready. It has its own secret key for signing tokens.");
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public void issueToken(Voter voter) throws Exception {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("STEP 2: A Voter Asks for a Voting Ticket (Token)");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Voter '" + voter.studentId + "' comes to the Authentication Server.");
        System.out.println("   [AS] Let me check my list...");

        if (!eligibleVoters.contains(voter.studentId) || votersWhoReceivedToken.contains(voter.studentId)) {
            System.out.println("   [AS] ❌ Sorry, you're either not on the list or you've already received a ticket.");
            return;
        }

        System.out.println("   [AS] ✅ You are on the list! Here is your secret, one-time-use ticket.");
        String token = UUID.randomUUID().toString();
        System.out.println("   [AS] Your secret ticket number is: " + token);

        byte[] signature = CryptoUtils.sign(token, keyPair.getPrivate());
        System.out.println("   [AS] The signature looks like this: " + Base64.getEncoder().encodeToString(signature).substring(0, 50) + "...");

        voter.receiveToken(token, signature);
        votersWhoReceivedToken.add(voter.studentId);
        System.out.println("\n   [AS] Ticket and seal given to the voter.");
    }
}
