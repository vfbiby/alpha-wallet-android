package io.stormbird.token.web.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import io.stormbird.token.entity.CryptoFunctionsInterface;
import java.util.Base64;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

public class CryptoFunctions implements CryptoFunctionsInterface
{
    @Override
    public byte[] Base64Decode(String message)
    {
        return Base64.getUrlDecoder().decode(message);
    }

    @Override
    public byte[] Base64Encode(byte[] data)
    {
        return Base64.getUrlEncoder().encode(data);
    }

    @Override
    public BigInteger signedMessageToKey(byte[] data, byte[] signature) throws SignatureException
    {
        Sign.SignatureData sigData = sigFromByteArray(signature);
        return Sign.signedMessageToKey(data, sigData);
    }

    @Override
    public String getAddressFromKey(BigInteger recoveredKey)
    {
        return Keys.getAddress(recoveredKey);
    }

    public static Sign.SignatureData sigFromByteArray(byte[] sig)
    {
        byte   subv = (byte)(sig[64]);
        if (subv < 27) subv += 27;

        byte[] subrRev = Arrays.copyOfRange(sig, 0, 32);
        byte[] subsRev = Arrays.copyOfRange(sig, 32, 64);

        BigInteger r = new BigInteger(1, subrRev);
        BigInteger s = new BigInteger(1, subsRev);

        Sign.SignatureData ecSig = new Sign.SignatureData(subv, subrRev, subsRev);

        return ecSig;
    }
}
