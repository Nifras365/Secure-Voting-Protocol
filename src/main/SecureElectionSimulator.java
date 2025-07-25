package main;

import crypto.CryptoUtils;
import model.Voter;
import server.*;

import java.util.*;

public class SecureElectionSimulator {
    public static void main(String[] args) {
        try {
            System.out.println("============== Secure Voting Protocol for Class Elections ==============");
            List<String> voterRoll = Arrays.asList("student01", "student02", "student03", "student04", "student05");

            AuthenticationServer authServer = new AuthenticationServer(voterRoll);
            TallyingAuthority tallyAuthority = new TallyingAuthority();
            VotingServer votingServer = new VotingServer(authServer.getPublicKey());

            List<Voter> voters = Arrays.asList(
                    new Voter("student01", "Candidate A"),
                    new Voter("student02", "Candidate B"),
                    new Voter("student03", "Candidate A"),
                    new Voter("student04", "Candidate C"),
                    new Voter("student05", "Candidate A"),
                    new Voter("student05", "Candidate B"),
                    new Voter("student06", "Candidate B")
            );

            for (Voter voter : voters) {
                authServer.issueToken(voter);
                if (voter.getToken() == null) continue;

                String voteHash = CryptoUtils.hash(voter.voteChoice);
                String votePackage = voter.voteChoice + "||" + voteHash;
                byte[] encryptedVote = CryptoUtils.rsaEncrypt(votePackage, tallyAuthority.getPublicKey());
                votingServer.receiveVote(voter, encryptedVote);
            }

            tallyAuthority.tallyVotes(votingServer.digitalBulletinBoard);
            System.out.println("\n============== End ==============");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
