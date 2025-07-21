package server;

import crypto.CryptoUtils;
import model.Voter;

import java.security.PublicKey;
import java.util.*;

public class VotingServer {
    private final PublicKey authServerPublicKey;
    private final Set<String> usedTokens = new HashSet<>();
    public final List<byte[]> digitalBulletinBoard = new ArrayList<>();

    public VotingServer(PublicKey authServerPublicKey) {
        this.authServerPublicKey = authServerPublicKey;
        System.out.println("✅ Voting Server is ready. It has the ID Checker's PUBLIC key to check seals.");
    }

    public void receiveVote(Voter voter, byte[] encryptedVote) throws Exception {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("STEP 4: The Anonymous Voter Submits Their Vote");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("[VS] Checking the ticket: " + voter.getToken());
        boolean isSignatureValid = CryptoUtils.verifySignature(voter.getToken(), voter.getTokenSignature(), authServerPublicKey);

        if (!isSignatureValid) {
            System.out.println("   [VS] ❌ FAKE TICKET! Vote rejected.");
            return;
        }
        if (usedTokens.contains(voter.getToken())) {
            System.out.println("   [VS] ❌ Ticket already used. Vote rejected.");
            return;
        }

        usedTokens.add(voter.getToken());
        digitalBulletinBoard.add(encryptedVote);
        System.out.println("   [VS] ✅ Ticket valid! Vote accepted and added to bulletin board.");
    }
}
