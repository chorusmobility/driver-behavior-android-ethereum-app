package demo.technology.chorus.chorusdemo.integration.uport.signer

import android.support.v7.app.AppCompatActivity
import android.util.Base64
import com.uport.sdk.signer.UportHDSigner
import com.uport.sdk.signer.encryption.KeyProtection
import org.walleth.khex.toHexString

fun sign(activity : AppCompatActivity, phrase : String){
    UportHDSigner().importHDSeed(activity, KeyProtection.Level.SIMPLE, phrase, { err, address, publicKey ->
        "error: ${err.toString()}"
        "publicKey: ${Base64.decode(publicKey, Base64.DEFAULT).toHexString()}"
        "address: $address"
    })
}