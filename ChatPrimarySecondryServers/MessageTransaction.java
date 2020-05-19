package ChatPrimarySecondryServers;

import java.io.Serializable;

public class MessageTransaction implements Serializable, Comparable<MessageTransaction> {

    private static final long SerialVersion = 1L;
    String mid, sender, message;
    int pid, clock, operation;
    public MessageTransaction(String mid, int pid, String sender,int clock, String message)
    {
        this.mid = mid;
        this.pid = pid;
        this.sender = sender;
        this.message = message;
        this.clock = clock;
    }

    @Override
    public int compareTo(MessageTransaction o) {
        if(this.clock == o.clock)
            return this.pid - o.pid;
        return this.clock - o.clock;
    }
}
