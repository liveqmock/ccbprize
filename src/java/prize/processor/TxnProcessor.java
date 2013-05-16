package prize.processor;

import prize.PosRequest;
import prize.PosResponse;

public interface TxnProcessor {
    void execute(PosRequest request, PosResponse response) throws TxnRunTimeException;
}
