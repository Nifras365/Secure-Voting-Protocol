package model;
public class Voter {
    public final String studentId;
    public final String voteChoice;
    private String token;
    private byte[] tokenSignature;

    public Voter(String studentId, String voteChoice) {
        this.studentId = studentId;
        this.voteChoice = voteChoice;
    }

    public void receiveToken(String token, byte[] signature) {
        this.token = token;
        this.tokenSignature = signature;
    }

    public String getToken() {
        return token;
    }

    public byte[] getTokenSignature() {
        return tokenSignature;
    }
}
