package prize.processor;


import prize.domain.base.TIA;
import prize.domain.base.TOA;

public class TxnRunTimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private TIA tia;
    private TOA toa;


    public TxnRunTimeException(Exception e, TIA tia,
                               TOA toa) {
        super(e);
        this.tia = tia;
        this.toa = toa;
        if (toa != null) {
            //
        }
    }

    public TxnRunTimeException(String string, TIA tia,
                               TOA toa) {
        super(string);
        this.tia = tia;
        this.toa = toa;
        if (toa != null) {
            //
        }
    }
}
