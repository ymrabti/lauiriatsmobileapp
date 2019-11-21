package com.example.annuairelauriats.ui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryFragment extends Fragment {
    public static ArrayList<Laureat> Laureats;
    private Dialog myDialog,dialogFilter;

    Spinner findbyprovince,findbyfiliere,findbypromotion;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ListView malist = root.findViewById(R.id.list_laureat);
        Laureats= new ArrayList<>();
        String laureatun ="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDADUlKC8oITUvKy88OTU/UIVXUElJUKN1e2GFwarLyL6q\n" +
                "urfV8P//1eL/5re6////////////zv//////////////2wBDATk8PFBGUJ1XV53/3Lrc////////\n" +
                "////////////////////////////////////////////////////////////wAARCAHXAeADASIA\n" +
                "AhEBAxEB/8QAGQABAAMBAQAAAAAAAAAAAAAAAAECAwQF/8QALxAAAgICAgEEAgAHAAIDAQAAAAEC\n" +
                "EQMhEjFBBBMiUTJhFCNCUnGBkTNiQ1Nyof/EABcBAQEBAQAAAAAAAAAAAAAAAAABAgP/xAAbEQEB\n" +
                "AQEBAQEBAAAAAAAAAAAAARESAiEiUf/aAAwDAQACEQMRAD8AgAGmAAAAAAAAAAEAAFAAAAAAABAA\n" +
                "AAAFAAAAAAJIJAgkAAAAAAAAAAACAAAAAKAAIAQCKAAAkgACSAAAAZAABUAABJAJCqgAAAAAAAAA\n" +
                "AAAAAAAAAACAAAAAAAAAAAAAKBIAAABAABQAAAAAAAAAEAAAAAAACKAAAAAAAAAAAAAIAAASQSBU\n" +
                "ABQAAAAAABAAAAEgogEgCASAIAAAAEAAAASCgAAgAAABAUAHQEkFXIq5kXGli0Z2LC40sWZ2LoGN\n" +
                "QZqRPMGLgqpFrsM4AAoAEgQAAAACAAAAEhUAEhEAkAQSAFVABAAAAAFAAgCQAQCSCSgAAgQSQFCS\n" +
                "CSCASCiASQBIACAIJCgIAEkMeDOUyLizmUcmyoI1ibIAAkEWSQLFkElE2QABJPJkEAXUi6aZkE2i\n" +
                "pY2BWLssGQAFAABAAACQAAIJChBIAgkACoAAAAAACAACgAAABIAABAgkgKEkEkQABRBIAUIJAAgA\n" +
                "AARJ0gsVnIyJbsgzWiySpJFSCpKeyiWQS9BEQVgMgokAgipBBIEgMAWTo0UkzElMrNbAiLtEmmRk\n" +
                "kAAEABIAAAAAAAAAAqAAAAAAAAAAAAIAAAEkAokgACQAEACAJBACpIAAAAAZZHujSTpGL7I1EAJE\n" +
                "0ZaxVkosoNiUaAownQaIAm7J6IJsBYsJgomwiCfAE0QyPJYgEFmtaKhAkAotF0zUws1g7LEqxIBW\n" +
                "QAAAAAAAAAAAABUEJkgAAAAAAAgAAAJBBIAAEAAACSLBQAAAAEAAFAAAjPIyi7Jn2TCNma3Exjou\n" +
                "oFoxNoxsjbOMaQljtGvEl9AccoUykofR0zWzNoIy4k8S/EniBhSJo14IjgBnQ8a2aPE0U/USoeCV\n" +
                "GyXFKP2wvxogNIqyatUQ1x8gQAAgXg9lC0eywrcELok0wAACASAAAAAAAAAM0SVRYCQAAAAAgkAA\n" +
                "QSAABARJAKJBAAAAgAAAAAAAAES0iSs3oLGT7NcaM1tm0ejNbjSJtHowTLxkRpo2UbIcirZREijL\n" +
                "EAEXSKIugHFDjRZEhGTuqIUPo2UUy0qUQOdY1dvoibVUkTO2/wBFJOgKdMh7EurITCAD7IAksipa\n" +
                "PZUrZdEkLok0wAgASCCQAAAAEASCCQMkWRVFkBIBAEgAAAAIJIJAAAAAAgAAoAAAAAAAgAAoGeQ0\n" +
                "Msm5URqJgtGiKxWi5huFkplQFWshsgATZDIshsC1hSKWLA15jmY8iyYG3MczKybAl7KS7F7IbKiJ\n" +
                "FPBaWyEEVT2GJd6CAktBXIqaY0WJWgANMAACJIBIUIACAJAUBBIGSLIqiyAkAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAQDLuZqysI7bJW4ukTRMUX46MtsQWktlQJIZNlWwIIZJVgQRZJUCbCZAQGiBBKAMq\n" +
                "y7K+SoqyCzKhCwiH2SUPJtDoxXZvFaLGakAFZAAEAAAAAAAkKAgAUCCJQEgAAACAACgAAAAAAAAA\n" +
                "AAAAAAAAAAAABKgS3SszW/KUzRTXRhHLBrZVzV6Zlt0uNmclTKwzVomU0wKt0VTtkSloiIF2VJZA\n" +
                "EMqWpsimBBKFP6JUWARdIRiXUQKMqauJRxApIr4LSKFQJIAReCtmyM8aNDTNAAVkAAQAAUAAQAAU\n" +
                "AARmWKlgqQAQAKAAAAAAAABQAAAAAAAAAAAAEAAFAtVoqi66M1vywyY/5bcUYJW9HXJNdGTVeDLb\n" +
                "KmiVJiREYuTpFRZOy6LrDxWyjIpZKMpOhzYHXCUFpkt4/COPmSpsDruP0NM54zLqdgbKKBTkLAsy\n" +
                "rFgDOSMltmuTaMkmEPJKjbCWzSPFO2yovFUiSyzY5RpLZBqM1AAKyAAIAAKMAAAAAAAGZZFSUBYA\n" +
                "AAAQAAUAAAAAAABAABQAAAAAAAAeQACLrooTZiunlMjKXZo9kLFb2RtjwcnSOrFiUI/sRiol12EV\n" +
                "mc81s6prRhJbA55Io0bMhxsDEi6NJKiOKYRCk/BeOSu0FFIiUbA6ITjJFpaRyxhNNUbu62FEy9lO\n" +
                "iUQRN0Z9lsn0UyfCC/ZRDkUbsImMeTKi2FNyOrwUxw4ouajFQCSCsgACAAAAAAAAoAAjMsipZBUg\n" +
                "AAASAIAAAkgAAAAAAAAAAAAAAAAgAAoEoiiUYrr5XSolySRWzOctEaWi3KdI3S2Z+nhq29s2VJ7Y\n" +
                "FchjLovOVszl0BkypLKhCSI6LACKLRILRA2j0HEiLLWRVGipdlWBVbZWac4210dGCKp2XtOLgaRw\n" +
                "1bpGuPHxLvEosksYoADTAAAAAIAAAAAAAAFiwCjMsiESgJBIAgkACASQAABAAAAAAAAAABQABAAp\n" +
                "/RrHC3G3oasjI1xYlXPI6RLlGCqK2Y5G59smtzy6JTxOFQRiVxpR6LTI0q2VS5MllokES5xXx8FY\n" +
                "5XLvs2j2MkYuPVMDFzI5K9mcuRnLkB1KMZeTPJFwkYqUl9mrbktgEWqyhdMCKolEigJTLp6KIsug\n" +
                "qWV8lmTjhb5PoIz9xxlVGkZ/o1eKM9xdGMoODpmsZtS3bIAKwAAqAAAAAgAAqAACgHgAAAQURZFU\n" +
                "SiiSSAAAAAAEAAFAAAAAAAAAExi5OjoWCMVcgsjCEHLwbe1GHfZpBxqk6JSgnbdsjeM9paiVlKUv\n" +
                "BrkyUviUjmivyRGpWcscqujCaOx54cXRyxnFzqRF1XqPI0krxqa6NnhhLE68lILh6dwf+gjBlovR\n" +
                "VkW10QbxGTrRze473oupgXinxK8d0FmSLNxn09gZygkirLy0ZsCCSABdMkoiyAsixWyQJs3x6jRj\n" +
                "PG1ici2J3BFiNlpkzjzX7IREZ3OjURg006YOqUFPbMMmJx2uis2MwSAzhQAAAAgAAoEAkAAAgAAq\n" +
                "iJRCJAkAEAAAAAAAIKJAAAAeQBfHByZMMTff/DpUOEP2RqRWPDH4uQk3Ptf6IVR2+zOeV+A0s4t9\n" +
                "KiHFpbM1kb8kty+wavBJ9mOTUq6NYZa7VkZMib2gjFfnRhl1M6HFXcXT/Zhli1PZFaYc0uLXIvjy\n" +
                "OcXZy49Tr7OqCcFFcbIqJIiOmayb8woyemBfJCOSF+TnljnD9o15UFPeyK5uX2WUq6ZvKEJ7Zm4w\n" +
                "RQeTktlbIaCAmwCAiyLFUSthVkzbFC3bM4x3s6YLRBacU8UkYYF8TpesbM8eoGkMkuESuBW+RlOT\n" +
                "nkrwdOJVRUWt9E8X9WRlzRjpdiE242wqssVvoynjcTojlbJ5p6aCVxg6cmFSVwf+jnlFxdNBjEAA\n" +
                "qAAAAAAAQAAAFUSQiUBIAIAAKAAAEEkASAAJStm2PFT3tkYUXnPiqgRuRdyWPrbKpyk7kRDfZLl9\n" +
                "BWWaWzK7L5urMYk1rldGncTIvjdpjVnhCdMT2RK7KtjUvlDM5N+TQpNasM2KwSc1Z0LI/eS8Iwwq\n" +
                "8l/Ra/51hHTPI0Vc1JVRGQzi9lETRmzZmclsyqtsEEpBQE0AIoUG0iHICxaJmm2b4sbb2FXxptnR\n" +
                "BaIhBIvKXBBEZX8VFGGafGPFdmnKvlIxjHnJykWCcMKVvs6KcYuQjitd0Tki1ja5FRyRuU/s61qP\n" +
                "RzQajNV2dbm1FAZrvot/pj3H9In3GDUBx5xpk8/tEpp/oI5ZRcXTKnZOHJbOeeJx2toJWZBJBWQA\n" +
                "AAAQACCiCUQiQJAAAAAAAAAACjpw4OUeUjKC1ZdTlF6evolbkXni4/iUirezoxzWRETh5RGtZtqK\n" +
                "pFGyZO0VS5PQakVcXLRaHp0dEIKKOTNnkptRYL6brBEvGEYyo4lmyf3ExySlbbGM665wjJmGTBXR\n" +
                "j7s07TNsfqW9TQw1hKLj2Ve0d7hGavRT+Gi+iNa5fT421ORgrU1f2enjUMUWmc3qMG+UQiZbRinU\n" +
                "qOtYW4IxyYJRlYXIhrRVououuisoteBpjNxQRIaDNik3L+kz+XlM6lCSV0Fkj5SKjmUJPwXji+zp\n" +
                "Tg/FE6ApjxpeDeKSKWg5jBrzroq3btlFbLRjZVQ/k9miiRSXYjLkBfwZ5Zfy2TOVKjP1DSjFDUxn\n" +
                "iTczqn0jDAt2aylbGtTyCyG9EJk1eUyl8qNFpHNuWTR0NjU5WUi2mZpk2Vmxhkjxmyh2SjGa2jny\n" +
                "4uHXRWLGQACBBJAAAAQiSESgJAIAkAAAAAJjFydIg1xqlYWLtcPjJf7MsicTfkmqltFZQcVf5QI2\n" +
                "rjfBJo3jmT/Ixa1cdozbIOqWNN2iyjGCvRhgyVak9D1LuNxegIzeo5fGPRyvbJbCV9ugKpMtBfy2\n" +
                "yOKp/Mtxg4JJ0DYq0yC6xz/plaK2m6emBPOS8svDLOL7Zmkn5RdQdrVgWyNsp7jSovlcfpozcfrY\n" +
                "HXi9QnjWtoTzxfaOXFpFpKwOjHPHJmnCLfg4Kaei6k/sLrql6eMmU/hKfZmpyXk2hmfkBTgqa0Y5\n" +
                "MEZ7jpnSsqfaJ+EgOH2Mi8olYpnb7S8FZYpeAOZYq7ZakjT2pkxwvyynxmiyZuscUg4wiRdjlnye\n" +
                "ki2ODjHo6I8GW+IXpySjJ5E60jNxeXPb/FHbOUUuzmnO5VHoGumMIpaHtL6M1JxhZWOSTfYxja2e\n" +
                "JEe1FeDLLmlFUVeWXtjDa0ioxmWnj5bRxqTb7OuGXjBWMXVeEkP8mnvQZMoKS0F1mnss0pRM6cXv\n" +
                "ZeLKljkkqk0Qb5sd/KJgVzoAAiAABCJKkgSAAJBAAkgF8WN5JV4CyJxxvZoXnHgqSM0zLrPPxJeM\n" +
                "+P8AgoQVmxeUE3eN0/oymt/JcWG/2XWS9TVoJrPi0n5Rny1R0+3q8btfRzyjv6C6ztpkqKq5O2RN\n" +
                "NLRSLsgXstZVLdmkFcipi0Xwhflk8018kVk9iMWwY1jHE1tF4xjB3FlEqQt0D6tPLyfyRnwT/B0Q\n" +
                "yFoDaEZL+iyWpf8A1kY5yS7LrJL7GGsWl5i0Wio1pl5ZHXVkKUH3EYvSvF+CPkjTjj+6IeOPiZDY\n" +
                "qmzSPRRY5eJWXipx7SYF1OS6DzSXgq5pdxKSyJ9AWeaQWWX2ZXbNI8a2BM8sq2zHm5PbLZH9CEV9\n" +
                "MDSCIm/2XTil0zLJ1ooxlJt9m2KMX2zOMV5OuHDj0Q1XJG4VFlMcGns1ksf7RCjjrTZTWOaDcuhK\n" +
                "LWM2cI/3GclDpsGsYRS7ZrkaUFRZOC0kV9R1HigMoO5UdcJ8FTMMEV5VMvJ1dkG6lGfTKTjx2ujm\n" +
                "wSfO0ztjJSX7CslLZjnhUrRvkjx2iG08bsJY4wSyDTAAAipKIJIYkEElAAATFWzoxS4NGeKL2yfJ\n" +
                "G47NTRhPFW0VjNx6ZtDKpaZGpcYdFWdU8SktHNLG4kb2VQgkgsrN8rqTXTEskZ6mv9lCrLrBLG1f\n" +
                "F2jmjfOvJ0cmnozhUfUcmtAW9uX0WxRfJujrjkxyWi+OMePgjcrgl2zWGoGuTApMrPF8Uoka+Mrt\n" +
                "0Wf4ovi9O07ZpLAPp+XKyKOh4GimSNBOdVgaIziXTLqcLNWijiXTDLqcsySWQNTmjI5SXkkDTBSk\n" +
                "WTXlIhEhF1KK8ISyL6K2ZzYFnkt9GsJ66OVPZ0QWgYvLJS6OeeVyZbLIwBlXgm2dMejLEjULiJEI\n" +
                "SZUacpbM32Xb0Zt7GrymL2Tnk04kRGfuI1Mb4mpKmY55cHxl/wBLY+yMs7yU1aAjDGvl4NLa2mRj\n" +
                "hwhcNxfghtSlUf8AgV0Y5c40ys418b0ykXSLL5zV+CIwyYZQ/wAGR6GZPjo5ZxUtrsrNYgAMpqLC\n" +
                "iUX+S3JmddcTRA5P6FjU5CUgaQj5KnLSCiocX/0iWOS3+SBMZuJRn/gJmrUcm74synFx/JUBrDO4\n" +
                "6ezdSjkRw2Sp07RFdM8N9GEoOLNIepf9RsnCa7DU9WOJlGdk8C8GEsUkRflYMozSWii2vxKziccW\n" +
                "5aNpSa6ZOKMVHumUmnYTFlnnH9mmP1Kcto5t/RbEvlYHdLPGK0gs8X4OaXYKO2M4yXZE8akc0WJ5\n" +
                "ZRqgNPYQ9gqvUNdouvUfoi9U9hlZYmXWdPwX5xfkHVcrhL6Ip/R03FvstwTJjfbkoI6/bRV4ojE6\n" +
                "jmRNm/soj2SmxjZjJ7Ot4NdmL9O2yHxjHs6oaiIelrstLFUSnxyZJcp0i2PDKT6NsXp4xblI2WSK\n" +
                "CWqLC0g8cjVZIsnlF+RidOdwkUcZX0ddx+yjlD7GNdMPbk0ZyxSs7FOH2Z5MsE6GHSkMOtmfqYuM\n" +
                "os3hniR6iPuRVMYmscbuSKZV/NL48UlKyMy/mMFi8JuPQgo5Z8vxkZctUXxtrrsMNncnTWzaEaRW\n" +
                "EXVy7K5Mq/GIaMk7deCq8sr412WSel5fZUc2SPGW/JFHVnipbXgxoiY56aJtlVK+y9J+TDZyl9Gs\n" +
                "YrjciiVbNeMuKf2UEolirjJeCE2jQsyLFp9ii6mBZT1UtooGGUvEpW4On9GbjKL+SounRb3LVTVo\n" +
                "GsiVJrp0WeJPeN/6M3adPQV0Q9Q1+XRssmOfk4XaItkFvXpQWvJTDNqiuS8kab6IjoDsc8c/yiYu\n" +
                "Eb+M2gtooyotwyeKkbY4SS/AyxXy0zdzkvIw1V2nuBW4t1TRp7jHOL7iiGqxSfTImn9GiWN/oOH9\n" +
                "sgusK/RY0UJ/pkOM/wC1BVUXe0VV+YFrVeUEc6k+b2axnL7Zmo3kdM04tBV/ekWhmldMxaZMPyKj\n" +
                "eWfi6olZk/BhNfMlaQGmT1MY6Kwz3ujknLlI0i6QHX76Mp+q3SRnKVRM8cHOV+AOhzckgg0volf/\n" +
                "AJZAZESzafhlbjY0wyOo6OZNt9nTk4uNJs5+PEDSBlkdyNcbXkzmvldMCcZvLIoRVmWLr8WPVvUU\n" +
                "UdGHJGdpGOdPkZYnT0dsoKcCLHE4u68nVihHHFOfZRuOFW9yMZZJTe3oFb5c96i9GaM0nejaKt1B\n" +
                "W/L+iourWo7k/wD+F18I12/LKtqCqLt+WQpBFq5aM545Q8WXs2b+N0RXlVonj5JjGyJu2oow0tC5\n" +
                "y/8AVG6uXnSIhDjFBzNxLK0Umi3wyaemZJklZ+ko8XUv+kbj3tGkcmqltEOHHcNx+iLKo6fRDIcf\n" +
                "Mf8Ag5fYbwIJe+iuxrN8p5NPRZZFLU1ZmwVnGjx3vG7/AEZNbp6ZZNro0WRS1NWDWDi0rMzpyQi1\n" +
                "8Jd+DJ4JIjUwxuyZdk4cclbZV7l0Na51riWi7Kx0iWxqcIsggkanKSdhFi6mIUmvJPNkMFTFlkaZ\n" +
                "dTVbRiWTAVj5XVBwg+pNESSZVoCfad6mSsclu0VJtkwW45H3QyKfGqHJryUnNvyFZ8ZfRolOukUc\n" +
                "pfZMZSb7BqfbySe+jeNQjXZVN12Ai/P9E82Zgo05jn9pGdhD4YZJqL/EmM1JVxRjney2PoDVtLwi\n" +
                "kp/ovqikkiYEMjRT1bvi6LVsr6iVKIGeFKT0zTJmcNIYIwk+qZz5HeWVhSUnJ22SnooaQgnubpfR\n" +
                "DWmOLm+6j9mnPiuMNIzeTVLohPZUXstHso2Wg7GtSNGbf/H/AKKQjy7L5HxjRCvOUqVLtm/p8UYq\n" +
                "8n5My9Ljb+cjpdpWRUSipOkyVg/Zk3svDK46e0U6Vlj4vsi67N2o5F8WcuaORPS0FXsmMnF2jOF8\n" +
                "dliudayipvlB1MzavtcZEXTNFNT1Nf7CystxdMtx5K0/9FpLiqluPhlacdp6I3PSjVdg2Uoy/ISw\n" +
                "eYsHysAWlFx7KvoM3yjk07NF6lNVJGMii3II9PGouH+R7KOTk49MvDNNeQa3eEo8Bdeo1tErPF/o\n" +
                "Y1PVYPDIe20dSnB+SfiyYvdclA6nGJRwixi9Rg2RZo8aKyx10F+K2ORDjIU/oJkGyCeL+iKf0NOY\n" +
                "kEE2NTkbKvYZaK+y6nKvGzSKSQX6RdQk/A1cQRZp7TIeJg+KWQ2X9ph4mFyM7LJ6se0yMkG1SBkZ\n" +
                "JvJk0tHRGBWCjjWiOcpdCFjTiyGn5RVOd1Zo1JdyKyz8mXquonRFxm6fZl62NRi/2Cs8GpGc4t5J\n" +
                "UjTB+ZTLKsjQYQqh+5EN27ZQkC9hS2UswyzblS6Iru8WbYMd7Z5mLPOLSb0eriyxjEi9Nckljh+z\n" +
                "mWRzdtjNkc0YQk4yKza7FxhDiYzqT+LZipZH+RKbTCrOGSO2rRCnZdZWkJccnap/ZAUmujaOVNVP\n" +
                "ZzPlDUtr7LJlHTKEZx+KMGqZMZOPRaSv5INSMwiRRUvlaE6VS2mS8dq8X/ChMZOLtFZFFPrvyiVJ\n" +
                "x8kZW5bWpGPuX/ldkXXZGUcqqS2Z5MDV10ZRnvR0wzpxqRFlcMk0+iMbTyb0TkyfzXRphkn+STCa\n" +
                "OLb1svGPHs0WPHLadMOM0tNSQGMpEImT+4leUfoCzlXke6/szk7KlG3vSXkh+okZ0RRBtH1DXZZ+\n" +
                "pX0cwA29/fRtDNFs4yV2B6a4teCeCOBTf2bRnKuwfW7xRZV4EcyzTvs0WadjF2tP4dWXWGK8GfvT\n" +
                "J9yVA2tKhEe7FdGDZVbYR0e8iPeRhLRVPZTHV70R70TnfRVgdPvxboo5qc2l4OeP5WVxy453ILG3\n" +
                "9TQTVaLrjN6dMh4pLrYbtIP5Ivk4qWyqxyW6LSxyl2NYZJ/NUW9X8oJF0o4+9spKS7YHNhTWRFM/\n" +
                "/mkd2JJytHJ6uPHO/wBkViCas1x4XIHxmotnK1XI9ScY443o83K6ySrphmsjs9M241ZyHVh0kEdL\n" +
                "2tdEY4XZpGLkk5aj9F9Va6KOcgtRNGG1KI2aE0BSM/D6Jca2toicNWiMeSpUystI76OjD1TM3BR+\n" +
                "S8kKXF2ijXJhvowdrT7OvHkUlsjJiUkGp6/rlJRE4yhpiLBfKmZ0ZPa5Lvya+o6RlF0ysIUvKDkx\n" +
                "KPF2vxZVkVHkvjlxKJOyJOgN/cHvMwg22S1JBGzzSMp578IhJvR0Y/Txre2FcnNkqbPQXo4vwW/g\n" +
                "okHnqb+iVJvwd/8ABR+yH6T6KrjSLrHF9ujZ+mkh/DvyEZPFJK4vkii/ao6o4lHyXUYedgxyo2Sd\n" +
                "aNaxrwSpLwFxjDA+2arCTz/ZDn+yiVi/ZPB1pmTn+yvJ/YReXP8AtEYy74leUvsnlL7BqZLI/wCk\n" +
                "qoyv8GW5y+x7khhqGm/6Rxk1+JPOT8kqTGJqqxyS/EiOOSf4mvJkcmMNZvFkflIsoZUtSLMDDVUs\n" +
                "39xbhla3MWxyYw1HCcf/AGMPVS1FVR0c2jn9XJOcbGKpilJS7L54vJkTKY029Gs8ixxV9kVOPEoq\n" +
                "5FcnqFHUDCeaU/8ABQGpnKUttmOWNmrZFX2GVcMIqL5K2bY6i+inQsDo9xsmc1wpHOmaKNlGhJAM\n" +
                "NhJAAsYeohS5x8G6ImuUGv0UqfT5Pdw0+0S9I5PS5Pby/wCDtnSXNbhLsrKqbXR0Y8yen2c1eVtE\n" +
                "f4BjsnFSRzvG4P8ARfFmrUjWdODf6DUrz8srZkS3cn/kgM1eLXFpnM2+TpmxWGB5G6qwKe4yG7GS\n" +
                "EsbqSIAtB07LNtlIumbJWgGNM6ccnDfZji6NANv4prtD+MrwYNDg34A6o+shLs0WaD8nB7TXSCco\n" +
                "vaA9HlF+SbicKkyeT+wO3jFlXjRzLI15LRytPsC8sT8GUlJPo290nmmGpXK5Mq5M6/g/BHCAX45l\n" +
                "bLKJ0KEC3CJWWCiW4m3tIe2D4x4jijR42V4SIskRxQoVIjZdMiQVtjZNOUgnhIcZDauRBAdoq2NO\n" +
                "UrujLNxlOpeDSP5GWT/yMusWJxY4uXxdMx9S371PwbY/zRl6ivebe2EZBJvosoeZ/wDCXLwtICEl\n" +
                "HvbDlZDIIBISb6D0BKNcc4x3IxDRR0EgGGwABUolMAI4JfDOzs9Plr4y3FgFStZweP5QdxfhlO1y\n" +
                "j0AVE9mkJuMXF7ACuKceLbKNgAWeo7IxyakpRAA7Wo5se0cWbD7buIARTiprrYScdWABeGvLN4pv\n" +
                "qQAF1jmvKLqM35QAE8J/3FJqa7pgAZuSXZV5UgAqPeX7HugBFlkZdZWABZZC6mAVVrJQAEqTXkus\n" +
                "v2gAi6yJjmgCIWvoUgApxX0KigAHKJOmABHBMq8SABqjxKOzle5ABtfGvkjPPUMrdW2AVisW7eyo\n" +
                "AAmMeTAIrfgsWNyfZzN2wAgaQjbAKj//2Q==";
        String laureatdeux = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDADUlKC8oITUvKy88OTU/UIVXUElJUKN1e2GFwarLyL6q\n" +
                "urfV8P//1eL/5re6////////////zv//////////////2wBDATk8PFBGUJ1XV53/3Lrc////////\n" +
                "////////////////////////////////////////////////////////////wAARCAHEAeADASIA\n" +
                "AhEBAxEB/8QAGQAAAwEBAQAAAAAAAAAAAAAAAAECAwQF/8QAMRAAAgEDAgUEAgIBBAMBAAAAAAEC\n" +
                "AxExEiEEEzJBURQiYXEFQjNSIxVTgZFDYqE0/8QAGAEBAQEBAQAAAAAAAAAAAAAAAAECAwT/xAAZ\n" +
                "EQEBAQEBAQAAAAAAAAAAAAAAARECEjH/2gAMAwEAAhEDEQA/APMOrgeuX0cp1cD1S+gNqmTWHSjO\n" +
                "oaU+kgJYMZG08GMgJAAKKhkdTdoUclTAuGCK2Sqb9pNYg5U7VEdyOCXVc7abvFAORJciCgJXWiiP\n" +
                "3RB1opERwWg0YCGQDAB2AAQBdAMAuvIADyAPcAoAAADk4y+qPg6zn4yN4J+AOJ5AGHYAEMAEAAAD\n" +
                "EACWWVQjq4iMXhkrLNOG/wD0x+wPQjRpxwjRJLCGAAAnJLLIlXpxfdgaAYPiJPog/wDkSdab3VkB\n" +
                "0YyyJVoRzIy5EpO8psuPDwWdwhPiV+sWyeZWl0xsbqKS2SGBz8qpPrmylw0Fnf7NgCpVOMcJDshg\n" +
                "RHB+V/jp/bPNPT/Lfx0/tnmGozQdXA9U/o5Tq4Hqn9FHRUKpdIp7odN+0inPBlI2lgxkESxDEUOO\n" +
                "SqhKyXMB08BWwFPA6uCDiqZOyl0I5KmTqo/xoC3gllvBAAQ8lkyyB0w3iWjGM4wp3eTKXESeNiK6\n" +
                "wnNQjds4uZLyS3KWXcK2fESb22IlXqN5IGohT5kvIan5YaQaIBTksMtcRNdzKwAdMeK/sjaNaEu+\n" +
                "5woaKPQA56VbtJnQndABjxUrUjYx4mOqk/gDgYdgDsAAAdiBAAFAMQIBLJVKfLrKT7E/sJ/yL7A9\n" +
                "Hn1Z9EBqFaeZWN4r2x+hgYLh1lybZoqcfBYACSWEDCwNpZAQyHVhHLIfEx/VNgbBYw5tWXTCwuXV\n" +
                "n1St9BG7lGOWjOXEU13bJXDL9m2aKlCP6oDN8RJ9ELhqrz7JGyS8DA8z8hCUacHKV7tnCel+V/jp\n" +
                "/bPNLGaDq4Hqn9I5Tq4Hqn9FHVLAqWBvAqeCKuWDFmzwYvIRAAwKGslzwjNGsulECplVOkmmVU6A\n" +
                "OOrk6OHf+NGFTJrwvSB0PBDyX2IeQAibHJkPcLCvcLDsNEXCQ7DtuNIikkaximhRibQQWMpQsTpO\n" +
                "lq5DgRcYOJFjolAzcSpiEhlxjuEoWYEG1Orp2ZnYVroI7U01dYFUV6cvo56FTTLSzep/FL6KPNeQ\n" +
                "7AIBgICAAAKDuCAAE+sIq9aK+QfWKb0yusoD2sJEupCOZI5YUalSEZOb3RrHhorLbYFPiYLF2xSr\n" +
                "T20RyaKnGOIjsBhavPLsCoNu8pNnQgAyVGHgtQisIpAAAAnKKywGBnLiKS/a5D4m+0IthG47HNrr\n" +
                "yxFIOXWlmVgMfyv8dP7Z5h3fkKXLhDdu7ZwmozQdXBdU/o5Tq4Lqn9IDr7Ewy0URHabIrR9LMWbP\n" +
                "pZlIIhiGIoEavoMjX9AFTLn0mdPJpLpIOOoacN3Iq5Hw/UB1rdESLRE8hUBYaRVjLUiLD0lWAKVh\n" +
                "oBX3A0iaxwZxLuRVgSmFwoktjNo0uQyoSW5T3Qu477AZyQlkvJLRRM42dzejN1IOL8Gb3QQeiQRy\n" +
                "zVpNCLqu83YzCAAAAAAAAAAFLqRNQqWUKYHr0f4YfRZjCvTjSjeXYXqU+iLYG4GGutLEUg5dSUfd\n" +
                "KwGzlGOWiHXpr9iPTL9m2XGjCPYCXxC/WLYuZWliKRqkl2KA5+XVm/dOxS4aN/dJs2ADONGnHsWo\n" +
                "pYQwAA7gDaWWEcP5X+On9s8w9H8pOMoU1F3s2ecajIOrguqf0cp1cF1T+gOu5K6hh3Mingxlk27G\n" +
                "MiqlklMkqGaLoMjSPQQFPJpLBnHZmnYDlqk0P5CqpFJ2qIDtQpK7GhMlagSAQr7EaO4NkXE3sBTY\n" +
                "llE3KjkDVMu+xABV3GRcZFUJghlCFcZLRpDuJk4C5QwWRXHF7kRz1FaTJNa6tUdjEiAAAAAAAAAA\n" +
                "FLsKoNiqYA7uCpxlRTkr7nWopYRhwMbcNH5OgAANiXOCzJAUBlLiILG5PPnLogBuBg+dJdkJUaj6\n" +
                "pgbuUVloiVemv2uSuGXdtlxowX6gZvif6xbDmVpYjY2UUsIYHPorSzKw/T36pNm4BHm/kaUadOnp\n" +
                "7tnAen+V/jp/bPMLGaDp4Lrl9HMdPB9Uvoo60HcSGZFLBlLJqsGU8lGbENiKA1h0mSNIECXUa9jL\n" +
                "9jWOAOaqZQ2qI2rdzBdSA7lgTYo4FJkrUDkSpbiZF9yK0YhXAARcckI0gVY0BIaKSIpWAqwWIpIL\n" +
                "2QpOxnKZUVKZLmzKVRGTqSfwaTXQqm49VzCJrCLYFSdiFMqumorY54tuVgjWo25XINq8dOn6MexA\n" +
                "gAAAAAAAQADwKXSOWAfSB18LWqclRhG9je1eWWkZfjl7JfZ2AYKg31TZSoRS8mom7AJU4LCRVg7A\n" +
                "Au4w2S3ZLqwWZICgMnxFPtdiddvpgwjYZz6q8sRSDl1ZdU7BW7sssl1YRzIz9Pfqk2UqEF2Iji/J\n" +
                "VI1KdPS8NnnnpflIqNOnZW3Z5pqM0HRwfVL6Oc6OE6pFHWMVxkFIznktYInkDNklMkoDSBmXDIIH\n" +
                "k0iZvqNIkVlWRzdzqrI5XkI66fSExUekqoroKxExsRGgMdOEqj2NHw9RK+QM4msTPTJZRcSK1iWi\n" +
                "IFojShPAXBMDOcWzGUJHWitjUHn6LPcmUNz0JQi+xDpRZWcc0IHTTp6VuVGCiO4XCqw103FZZy8N\n" +
                "TcalprB2rbJMmiI5+Mfuicx08UrpM5whAAAAAIAAACB4Yn0j7MXYK7vx38L+zrc4rMkebwUHUclq\n" +
                "aSO2PDRWW2BTr013uOElUV0v+xqlBYiTUbh0gRKVa9opWFpryy7GlJtrc0Ax5F17pNjXDwXyaPqQ\n" +
                "+4EqnCOIopLwMAhDFjJMqtOOZpAWBi+Kor90ZvjoX9qbAy/K/wAdP7Z5h2cdXdaMLx0pNnGajNBv\n" +
                "wvVL6MDfhcyA60URHBSyQUiJ5LRE8gZsllMkoCo5JKjkByyaQZEslRwQTVOSR11TkkFdFDoNnujD\n" +
                "h37DdYAxmrGbwb1FsYMixvGbhTSXcXNmncSalSXlDS9tyNtI19WzQnkxb92xcHqYVrFlXIwhXINL\n" +
                "jTM7gpFGyYXM0y0FUAgKG2ZOdmW2LQpLcqMJV5t27FU25OxbpJCitPYB8Qv8Rx9jrrO9JnGRmmAC\n" +
                "CAAAAAAABdmMSwUdn42O02dx53CcRGlFxauzV8bJ9MCI7SZR1ZOLn8TPEbA6XFTzKwHatMVlIl1q\n" +
                "adtSOT0sszq2/wCRxo0IbupcK3nxdOL8mT47+sGyU6EXdRciuekvbSQQvVV5dNMVuLn/AOovU1Oy\n" +
                "SIdSpLM2FaPhqr66tv8AkT4ahHrqtmT+ZNivFAbL00MRcivUQj0UUc2tITmEHG1pVYwvFKz7HIa1\n" +
                "pakjI0zQb8LmX0YHRwmZfQHRHBSIiyyCokzyUhTAyZBbJZQFLJI1kC5FQwTIqGCCapyzyddQ5J9Q\n" +
                "GnDPJ0o5eHe7OpAKRzyVmdDMpruFiaTs7PDN9H+M5e50U6ja3MukqHF+BwUoPeLN03bAXlIKi4h1\n" +
                "FpEtyAHYEMoaGpEXFcK11BqM7hcou+5aexkiKk3a0So0q19GxEaus53TqSd2XTi4ZCN3vFnKdF/a\n" +
                "znIlIBiCAAAoAACAJWMjYktmEdVLk04pqLmzT1DXRSSOSM3FW8DdRgdLr1X3SIc5yzNmGti1PyFb\n" +
                "NruxaomNwuEbc1LApVTG4XKL5r8C1yZF0LUQW5N9wuQ5C1MosCNTFuBVTCMxsRWVaPk34ZWlL6Mz\n" +
                "Sh1S+iDSL3NDL9jS4VcQmEQmEZMllSJZQkUsklLIFywOAn0hAgdQ5anUdU8HNUW4UUOtnWjjou0z\n" +
                "siEKRDLkQwMpqzCLsy5K5nZ9iNSt1PYqNRowhI0RG9VJ6siAYDQCABMVwYgHcq5AyqdyXKwxpKxU\n" +
                "RzGCbZWlCk1FAKb2MyXJt5J38kZ1dxXRIgi9SFqJuGoCrsLkag1FF3FclsV2Bdw1IgLAXqFqJsOw\n" +
                "BqC7HpDSBIWL0j0gZ2CxppHYDPSPSXYAI0jUShgZyjshaS59hBmqLo9TIKpdTFFSfuLi7kSyVF7h\n" +
                "a2iEhRHLARlIllMhlAilkhFIDR9IQB9IoZIKlg56uTplg5quQqKfWdkexxQ6jrjgItohlvBDAhkS\n" +
                "bT2ZozKrgUgTRpCSe1zluVCTUkRuOuwXHkTRFK4XJYr2KLYibhcCgIuFwq7jTI3IlU7IqNZTUVdn\n" +
                "PUqObFJuWRaQmlcV2VpHYIi7AvSGkCLMdi0h2QGaiPSXYLARpGolgBOkNJQWAVgsMCAsFgAqgLDA\n" +
                "gQAACAAAaAAAUgCQFjNBVPqZJVPLCKluEMg8Ewe4V0U2VIim9y5BGTIkXIhlCRaIRSA0WBR6gXSK\n" +
                "OSDR9Jz1jp/U5q2AMV1HZDCOJZOyGEBp2IZZDAhmdS7VlubaXJ2R1UqEaUcXYWPOjw1WX62Ljw+i\n" +
                "Scmela5lUpXRGmQCcXDIIipaJcTRkXAzcQ0ssDSIUGWoDAuB3t2M6lKUpakioTUpNeDRMiOVxcXu\n" +
                "I62lJbozlRy4gYWHYpqzsxWAQDYgAAAAAACgAGQAAACGAAAAAAJjEAAAAAAAAAAApdgCXYCxmgqn\n" +
                "1MnsOGWEVLBMclEyVpAbU3uayMaT3NngDJkMuRDKEUiSkBosErqHHAlkg07GFbBv2Ma2AObuddLp\n" +
                "Rys6aXSgrUVm3tkuEHM66dKMFjcDClw2+qbtY3krIsTQVm9hXG7xlvgiXZmVN2eSXBWC3yNbv4Kj\n" +
                "N00+5LoX/Y2Xd+RArmdKSYnCaWyOiS3FZ77lHM9SyhaW+5u43yLRcqaiEUntksOXbe4WAYm7WGkD\n" +
                "AGoyyjKdK26waGkenciuJgaVoaZfBmQAAAAIdn4AKAAqMHLAEgaciXkmUHECQA2hR2vIDG4HTy4e\n" +
                "DOdK26AyF3E3ub0ad1qkgM+XJ9hOEllHXYNGog4wNZ0ZLdYFToynLfBQ6dJyV3gt00a2srIQHLVh\n" +
                "oaIN+K6YfZgWM0DhlkjjkI0FJCTY8hTpPc6Hg5Y2gzV11bABIhi5t+wnMqAolNFAaQwL9hwwJ5IN\n" +
                "FgyqrY1WDKtgDms27I9Ph+G0xi5mXB8Pp981v4O1thcUklgZmpX2GmRVgK4AD3MmtMt8GopK6sBj\n" +
                "KNtxJ3XhFPFibO7sBWz2Ias9ikJ7pgifd4SHtaw5IVimkG/wN4ewNLwBDf692O2oeeyJvpe4D5b8\n" +
                "hoKU15uylgCFTKUdihNAZ1IalYwlRtg62Q1cg5I05OVjaNJLJqkkJkUtK8Gc6Xg2QMDClR3vI2wA\n" +
                "MoBSgmgT3KAxpUdM22asYmQJZKsQ3YtMCJUot3KtbYYgExxZLYr7gaCwCewADJeRslsDLin7YfZh\n" +
                "c14h3UfsxNRmgcdmxdgQRVxNibJI0bkLUICguwuABBHJ0I50bxftA0gEshEJ5CLWAhHXVSFB7HRw\n" +
                "0FvJ5Ct4qyGAEVLaAdlkl7uwFpjJSsguUMLiuAETzqXYWm8dSeTQwlJ0pf8Ar3IJk7bXsOD1vODR\n" +
                "6Kiulcya0taf+QrTDC7eEHbcLKzyioT8d2DVx7LAsfQPpWJ8F57ky3aawgkmDvshoLX3GsFw03L5\n" +
                "EndDsrboWwXQIdxPBKEyZDbIkzKmnuXe5hqNYvYCiWNszkwHfcpSMblwdwNbkyBsmTATY4y3M5sU\n" +
                "JAdCe4MlPYTYA8Gercp4MpOzA2jLaxaMYS3saFDbIbGyZAY1ndRIHN7iLGaRpRoTryag1tm5HY24\n" +
                "StyZzdr3QRX+n1f7Itfjn3mi3xs+0Besq/1SI3gX42PebKX46n/Zmb4ut8Cdes/2KZW3+n0fLJl+\n" +
                "Opvpk0Yuc3mbBSl/Z/8AY1fJz/HTirwlqM+XOmrTjY1jWqR73N48TGe1RBLHJEJnXLhoTWqmzlqx\n" +
                "cNmGMEN2dqTjGKRw08o743uiLFik7DJavIKccDBAAZEguDKALjRL3ZAXBrUrMqKJq1Y0luRUqHLu\n" +
                "uzJ21WM5cQ5O1rDd9aaCNFv9Ci7ti9zzsh7JXRqBgCdwbtuy6khbWvYYJbAC0nsF0DHfZMil99xN\n" +
                "bDe4ngqfSS+R9hIbwKrJ5JZczN4MKzlKzN4dJy1OtI6KfQVFkSKJkFZTdmXRd0ZVepGlHLINrEsY\n" +
                "mUZVOkiD3LqYM0rRv8gdXYH3BYEwJZjN7mzMZ9aAdN/5LHQc0fbxB0gJ4IkWTIDnqZJKqZJLGKOx\n" +
                "dPLJHTyxWufrRMYgMO+AYgCmACAoQAVMVCcobpnTGUeIhaW0jkGm4u6DF5HJnSrpPDex6JlRmq0b\n" +
                "tbo1KxhMUX7h2BKzuEUyJSHJk2uWBXY7OxSiJz0gC8DtuJSuGqxAq1VU4XW7OKV61RWfteSuIeme\n" +
                "pP8A4HwsnK9o2j5Ip8TGMKa/siqd50k++DPiIylpna6RNOuuar9+xR1KI18C2vdvYNS/UrI2bWw7\n" +
                "Rv5FfdIdrCFoe7uLV82GGzykUhXurLcE9reBqVtkrEt2VwfTsH2Qpx7tlX7sLgWQYWCyXcCZboyZ\n" +
                "tbZswZkYzX+RHRDaJhPrRvEBkvBRLCsZq7NVtJESyad4gUJjEwM5YJe1BlS3WxXLvSak7XAqPQhs\n" +
                "SkoxSW5Ln82C4GjOSbZer5BtIGM5RlzlK2xvdGepeR3uDF3JYgC+WNVWsQaVltEgsc7CKp5ZJdPL\n" +
                "FXj6sAC5h6AADAQABQBcAABiGQdHCRetvsdRhwytTv5NzTjQhdxSbeA7IIYgZEmVDnOxm3cmUtxx\n" +
                "3KLQpS2E3YwnO7IM5RlWq2WDpVqcVCODGM2rqKH7vDA35q027GT0vEUTaXhlqlJkBTqK+mTt8m1n\n" +
                "p2ndGEuHTzIiEpU3a90UdUYpq0d2F+19/BEakZ4eljbs943+QKv3lshRbbuO9N9ypKLtZoCGKWyK\n" +
                "svIpLsVMJK6RVvIXSM5VLAXjYErmHNnfp2HGo/AVrOVlYwZTd3uJmaMZ9aN0YVMo2jgCiWMTu8BW\n" +
                "cjRu2lidPGp2K1JYV7AxVnb4E3FfJLk3lkhqRTl4Vic/IGbnLW7LZBcVOaWxnKSdkE7ar+RO1jWM\n" +
                "VUtO1hwSbd8GOrc0veOzLjOtLRBNImMY6Ore/c1mqLhs1cuLKAJg9UblHOusrKtiJka1sRMixy6+\n" +
                "mVTyySqeWKcfVgIDD0GAhlAAhgABYCAALBt3BXbS6EjbsZ08ItlcaTQrg2RJlZNyE2QJtmg2twwi\n" +
                "NQOQQqk9jBN+C5O7JpS0u73VyK1hW05gWuJ26So1ab/VFJUZdgI51/BUZ3yyuTSfcfIp/wBiAUYS\n" +
                "eQdGMltYOTBfsUopfsByz4ecd0SnVj9HddeRWi0UcfNk8xQ1Uduk6nSgxcmJUcyk74ZUZTS2R0Kl\n" +
                "FMehBXJpm2NU33R0uKQmEZOItJvsS4lGMkRI3nHYycV3Zmq56nY2hF28A3HtG/yDbZGsU3GL8ic2\n" +
                "8KxIBqQBYADQsABYKDObkk9KyaCDNjmalYm77o63ETgvBdZvLOhGnOM3O1+xhqd9jp5SYuREus+X\n" +
                "Pcbd3srG3JiWqUV2GnkUlaCLElYqxl0jGviP2ZG1fETEscuvplU+pkhGehvYU5a2Cxm63hC5rM47\n" +
                "eo1sBg6kn3FqfkYnqN7ryLXEwuIuJ6bOr4Quc74MhFxn1WjqN9yqalUqRiu5kjt4CF5ufgHqu+Mb\n" +
                "KxTwQ5KOTOVbwRDkZslzm8WJbqPuiovCFddzP3vumFpd0UatRayYS2YTx1WZhC7nu7io1l7Y/LNK\n" +
                "cNlsFBXrNyV0jqUo3wQccoaKm2GaOlI3qU4zi1hmdCTacJPdBWbU4ivLyzq27j0x8AcmqXlkurJN\n" +
                "27HZUUIQbsr9iKUae8bXbyBjFykrotazSEVRqaf0kbaQOZSn8lqpJZRvZeBOKfYCY1Ey00RyxaZL\n" +
                "sBbJJc1HLuZyrN9KGrjXZZdiJVkukxbbyIa1OVSnKXciwwI1hAMA0BDABAPuAAAAEAAAUCGAQgGA\n" +
                "CGAAAAMDGviP2ZGvEYj9mRY49fQRIsmRazEiGIjQAACAAAAEMQGvD0XWnZY8no66dGKhDf6MKcXT\n" +
                "4dKOZdyW3F7dQq43i3O7k9KNVCNrqNyOHhJw1VN2b2IIjTlqv7UvBFXhnUldS0m4NM0ON8JUj0zu\n" +
                "Q+dT6o3R3WZMt0EcFSSmvBhF2mdlein7o7M4+4HZSrqFPBVLiU5e5Wucl0qZGog6qtdzlttYSqf5\n" +
                "Iz8ZOdTK1phXc4eod4uyWB07taH1I56HEaIyi8GfOnqvqdwNeLbUlFMwhOUJNrLG5OTu3dk3AfMq\n" +
                "NWcn5Onh68prT3RzJXwjalFwkpX3QWOpSmPXbJlOtKT22Mrt5ZGpy3de2DOVWUu5IBfJAAEawAAF\n" +
                "UAAEAAAUAABAAAAAAAAAAUAAAQAAEAAAADEMqseIxH7MTbiMR+zIscOvoJlkoiQZSHcACgBAAGlK\n" +
                "jOq7RRmdvCTUaLXcLAuBdt5IPQv+5tzGNTYbw403y1F+Co0oQWNT+RahqW5FxWuoltBf9idWqv8A\n" +
                "xoeoLhMQuLs/fBxNoVYzV4u5m0pZVzGVKVOWqk7LwVLHZdBsznpV9XtnszYus4U0jzZwtVfg9GTO\n" +
                "Kt13QRlJJi5aQwIFoQaPkBgGj5FYdxNgOEJTlpirm6oQpv8AyT1S8G3A6FTv+1xz4S0pTjK7YWMd\n" +
                "uysMOVWX6XFaos02R0mGIN/6sN/DI1piDfwwv8MGwAF/hiv8MGmAX+xXBpgK4XC7DAVwugaeQFde\n" +
                "QugbDAV0F0DYYCuvIXQTTAV0O6BoALryF15KAAAAANg2AAAAMuIxH7MTbiOmP2Yljj19MiZZE+xW\n" +
                "UgICKAAAAak1hiAo1jVki41/JzgmRqdO3mbXLpycpWMaMeZS+UzujBRirEblNILDAilYdhgEZzpR\n" +
                "mvnyOm2vZL/ssmXkJYJ4OOcXOokjpqS2OXVprXNMNPSTts0L0tX4NY1fk0VQDkfDVf6i9PV/qd6m\n" +
                "GtAcHpqv9Remrf0PR1oXMA5qNOcYvVFpl65rDNXVTVvJnYgh1qvZoXPrLwy5QI0BT9VUWaaD1U/9\n" +
                "pE6GGgGq9XP/AGf/AKHrH/s//SNDFpA2XF3/APEP1S/2jHSw0Mia29VH/bGuJg8wMNLDQU1086n/\n" +
                "AFDm032OfQw0BddPMpeEGuj4RzaGGhgdWuj4QKVF+Dl0MWkDsvR8ITdHwjk0selgdX+HwgtR8I5d\n" +
                "LCzBrqtSfZBoo+EctmFmDXVoo+EHLo+EcruHuBrp5VL4JdOnqtp28mHuD3eQa6eTS+A5FI5ry8jv\n" +
                "Lywa6ORSH6en4Oa8vIapeWDU8dTjTjDT3bOU24ltxhd9zEsZoIqdiyKnYqIAQwoEABAAAAAhm3D0\n" +
                "XWn4SCxrwKlqdunud5MYqEdMVZClKyM11kWBEZakWRQMm47gBM3sMxqy2CJnL2nNUu5XNHdhpKxW\n" +
                "Udd8mq12ydFOnFxxuPlII51KSKU35NHSIdJ+AHruO98kaGhgaU7Rmnc6rQmtjhRak1hgdTpEumzL\n" +
                "nTQ1xL8AW4MND8E+q+B+q+AHofgWh+A9UvAeqj4Aeh+BaH4H6mHgpcRACNPwGn4L51PyPnU/KAz0\n" +
                "/AW+DTmU/KDXT8gZ2+ASNNVP4Hqp2ygMrBZeDW8H3C0QMrLwFl4NtMQ0IDHSvAWXg20IOWgMbLwF\n" +
                "kbctC5aAycUGleDXlhywMtKDQjTlhywMtC8BoXg10BoAy0INC8GvLYtDA4uNSjGFvLOY6/yEbRp/\n" +
                "bOQsZBnU7GhnU7FEiAAAAAAGtwKpvTUTZFbUeGlN+5WR206caUbRM/UJ4BVlfIbkbNkJa3Z4HqXc\n" +
                "FJJ7EdItJJWQmyXUSyQ6qINVgipPSKFW+Tmq1feErdVncU/c7nNzUjalLXFvwVm1VhFEsMNKdS2z\n" +
                "OhWscSyaKbSA6tg2sc3NaGuIA6NK8EukmZriENV0FN0fAKFluCro1hUhNWYGWlCdNHRy08E8sI5+\n" +
                "ULlnToZOhgYcpi5T8HRpYOLA5+WxOm/B0WYrAc+h+A0PwdAAc+mXgLSOgAOe0vkLSOiyCyA5/d5H\n" +
                "eXlm9kFkBhrl5Gqk13NdK8D0IDLmz8sObPyaaEHLQEKtPyNV5FctBy0BPPkPnyHy0HLQAuJZXqfg\n" +
                "jlC5XyBp6hD9QjLk/IcoDZcQg58bmDphy2BH5CanCnbyzjOji4uMYX8swLGQZ1OxZFQogAAAABgI\n" +
                "YgAd35GmyQIuuiNZ9y1V+TlC4anTeVV+QjO+TC4IYenQ5pYMJSuwYgloOvhP45fZyG/DVNMtPZgd\n" +
                "QslEt2IE9gREnuOErgapLuVopvsZ3BS3CrdGLwyZUWsblxZdwObS+4rtfB0uxLjFgRHiJx7j9VUI\n" +
                "lFdhJAaesq+EXHjf7ROeSM7BHeuMi+wesj4Zw2AD0PU03karUn3PO3DAHpKdLyh6qflHl3DU/IHq\n" +
                "2h5QtMH3R5nMl5BVZeWB6eiIctHnc2Xlh6mUe7A9Hlhy/k4YcXJ4L9VMDr5YaGcq4qRS4t90BvoY\n" +
                "aH4MlxqfYr1a8AVpfgNL8CXEx8D9RAAs/AWKVaDDmwAgDTVB90O8fggyA19r8DsijIDTSg0oDg4/\n" +
                "pp/bOU7fyUdMaf2ziLGQZ1DQzn2AgAAoYCAAAAAAAAABqLfY0VJ9wuMioxbwjaNNItbYI1OWcaP9\n" +
                "nYfKii2A1ucp0xSwDSSvYoma9pNSzF0at9mzV2OFNxdzeNfbcMNWgUfBHOg+41Wiu4G0KUpZNFQ8\n" +
                "swjxSXcr1PyBrKKj3M3IzdXV3Goyfa4FXYbjjTl4LVN9wrGTtktWvYJxg5puS27EuHtl7leXcgUt\n" +
                "zO25rZabLsQ8hCsAwZRL2JbCc0mZuaAoG7EOoQ5XKNXJBC7u+xkt2b4WwATJbFEywQOng0MoGoAT\n" +
                "J7MomQEU3k3WEYQ/Y2j0IBktjJYBGTcrXHBtp3fciP8AIVT6pgN1JLuVCpKSe5nPI6XcDTXJdyZc\n" +
                "TKLywZhWygOlcTK2WNcTJdznj0IGBpxFZ1YxT7MxBgWM0EVOxZFTsUQADsAhlKnJ9i1Sd9yLlZWG\n" +
                "oN4RuoJFYGtzliqL7mipxXyUA1qchJeAACNZAAwCkAAAAwE8Bms5pGZrlkyiVyrMBtCCKQyQuBV2\n" +
                "u41UmsSZNxXA3jxVaP7DlxdWUbNnOIGtdYtb8mYyYrr4ZuUJXKkjPg31I2mBApMohgc037mSVNe9\n" +
                "ktWKgAQ0ENG66UYG0N4IKZLwymSyBQybGCdmbLAUyWMAMoZkbw6EYLrZtDpKGSyhMgz/AHTNIfyT\n" +
                "M3tJFx/ll9AKeR0ssU8hS6mBbMK3Y6GYVsIQOHQgYqfQUyohgDyAiAmfYAKLpU4yya6IxwgAjcAA\n" +
                "AbhAAGWwAAUAAADAAIEAAUAngADNSsgAFcqmSM3kACAQAAwAAEMAKEAAQb8J/MdEm9QAFFiZABBg\n" +
                "1/lZFTIAVGYwAoDan0ABAxPAAQQbrAAFHYTAAI/c0p4AAKYmAAZy6kWv5f8AgAAU8jpdbACi5GNb\n" +
                "pQASIVLpKYAWiJZQIAER/9k=";
        String laureattrois = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDADUlKC8oITUvKy88OTU/UIVXUElJUKN1e2GFwarLyL6q\n" +
                "urfV8P//1eL/5re6////////////zv//////////////2wBDATk8PFBGUJ1XV53/3Lrc////////\n" +
                "////////////////////////////////////////////////////////////wAARCAHZAdwDASIA\n" +
                "AhEBAxEB/8QAGQAAAwEBAQAAAAAAAAAAAAAAAAECAwQF/8QANhAAAgIBAwMDAwIFBAICAwAAAAEC\n" +
                "EQMSITEEQVETYXEiMoEFQiMzUpGhFDRisSRyQ4JT0fD/xAAXAQEBAQEAAAAAAAAAAAAAAAAAAQID\n" +
                "/8QAGxEBAQEBAQEBAQAAAAAAAAAAAAERAjEhEkH/2gAMAwEAAhEDEQA/AO8AAw0YCADPqHWGRxI6\n" +
                "+qf8Je7OQsDsdkjApMuyI8o10hE2Ox0NRAEzlmtUpu+WdajTvwcjhe98hqJgk5Uzo6VJa2vNGHpv\n" +
                "yjfBFrF+Qy3sLI3oLYF2O/8Aszsd7FGqd0XsYavqSK1FRqGlEKQ1MB6EGj3DWPUhgnS/IVIvUgtD\n" +
                "Bnv4Fb8GuwqAzs4/1DeK+DvpHD1q1ZIx8tIDqj9MILxFDsuUSNIDsBaWG40VSJePG+Yr+wWwsCX0\n" +
                "2J/tJfSY35NVJBqQHO+jj2kzN9G+0l/Y7NS8hYHny6WUfB1dPHRgS9zSVUKuAKsLI3C2TRVhZFsL\n" +
                "LovUGoiwsaL1BqIsVgaWgozseoKU1siRzZNkDH3JsdgM6uDlhvkj8nUAAAGVIBiA5+se0Ucp0dY/\n" +
                "rivY5yhjEAFx5Rdkw3ZVBDsdiSGkA9T0y+DBcGuRacU37HIm/JGp1jY6MNLErOLXLydkE9EfgqNJ\n" +
                "VV3suTL1oM5+oy6npi/p/wCyMdsI6MmeMXSjfuxR6hVvEqGO4/UN9Oq2Jq4XqRcrspTTMp4ZRIUn\n" +
                "F+Ga1MdSkVqM8TjlW20lyi/TfkB2OydDFUgi9QaiN12FbLo11C1meoNSGmNNRzZPq6vH/wC1mmtX\n" +
                "yZx36teysDrcw1GVhYG1oDKw1AaiI1D1AMKQtQWQG3gKQtgAUlsHcb3Yr3sKLCwEQMNhCAYqALAV\n" +
                "CY7Aom2FgxUApS3FZMnuxJgaWFkWFhXR0++T4Ok5uk5kzpCAAEZUwEAHH1X878GJp1DvPIzZQDJG\n" +
                "gLiyrZMRhFWPUSMCpqWTFJR5Of0cq/Y/wdmFbM00K7A81xle8X/Y6uoy+njpcvY2m/TjS3b4RyZl\n" +
                "qUZPnhhcYKLlKjrxY1BcbmeGG2o2M2tSKspMgLoRTk7McsKV0aah7SjQSubHPROMk90d2s4Mn0Sd\n" +
                "8HZCLeOL8o2w01If0kaWGlgaUjHPkjijtvPsic2R4sbr7nwcLuT1Sb37kGrlJ7y3kzNzlf2jt19L\n" +
                "tE+q1s0FWpa1cbs0hNqS1Km9jH1YtXv8MfrLwio7FFvh8hTRl0z1yvijrtNWBhuFmzSFoTCMrHZb\n" +
                "gJwAmwseglxCnqDUTpFTCL1bivYUFbfwVWwUrHYqCiB2FiABiYCAbJYWJsAFYWJvYozb3AQyKYWI\n" +
                "AOzpF/Dk/LNzLp1WCPvuahAIAIoDuAEHn5HeST9yGNu2SzQYIkaCtI8DTJT2GEUiiCkB04fsv3NC\n" +
                "MSrFEsI58+pt6fgyknUYt2+5rKa0vyZQ3nZltoqSoE15BmOSG9amiK21JP6lXuNq0cmucLT3ihxm\n" +
                "1suCprdpoFKhRyeRzSa2WwVlnWpwS7ujqUqSS4RzpXOHs7NTUYvrRTY1NmVBQRh1MlkzvfZbGcrj\n" +
                "TT28Gb3k1Y1Nw53RcFKaeybXsH0rnf3FcZdqJcX7AafCi15BP/ivwZJST5o1g33dgaQbjKLqkdqi\n" +
                "623R5+tM7cU6xx+Aimn4Dcayew9a8ATbDUXqiL6QJ1ewrXgvTFi0e4VDoIpDcH5FpaTAI0mCe24R\n" +
                "X0ux6VSt9iA2FsJ6f6kLZ8OwKoWkKaFuAUJodisCWiWXYm0BmxPguiZqkUZgJjIoABpXJL3A9HGq\n" +
                "xxXsULgAgAAIoJyOscn7HPm6neoceTOOWT+lttMuDJks20RfZoPTh4f9y4axA39OHj/IenD+n/Iw\n" +
                "1mhmmiPgNMfAxNSig4WxLGGujp80ptw0fbtZWWUoz0rirsME7xV3Qs3Cl+C2fCeubJL6q8BF1TMp\n" +
                "S+qwlLwc23SqDJCM4mCl9Ngpyf2hT9KXFl4sFN6isUVVuVyNbpAcMp/W1dJG2ObqmtjOce9WhY4N\n" +
                "zVP6So6YbK/I7EAZVaBNCEVHDl/mz+RIvqo6cia/cZIqNYrfmin7O38ELZbGkIslakEcd7y3NoxS\n" +
                "QqKSM61jl4m0duNfw4/Byyx/xParZ0423G2/heDWs4ug3CwDIthqYCCnqHqJBgPWxSzKKpyV+CJO\n" +
                "k2cWWW7ZR2yytx+ivyzNzXe2/Jxa32Y/qlxYR0PM4umJ5r3TpoUMM8mzLfSSS5GrjbF1GrZm1nDH\n" +
                "Hpf3b2dMJWt+SDTYTFYrChol2DYnIIGRNukmVqIm7YVIyRgNF4Fqzw+bMzfpFee/CA7mIYgA5eqz\n" +
                "fsj+TfJPRBvv2OBre3u/CESkk38BdClrfakTv4NI3TtWMzg7TKTtFF2FkWNMgoV7Og4FxP2kBT2J\n" +
                "sdtfBLYG3Tyqbj5RrkWrHJdzlxOssfk7OGaR5bYr9zTqoennf9Mt0Yt7mGlvVKlEqFwe5eFKtuRz\n" +
                "mk6kjLWMnkrJa2NY5r9zOSTW1NGa+mSXnyU10waaocFVmLlXybx2SIWqFYgKydjsiwQEdSk1G2cy\n" +
                "TXY65JSjTMJRb3RQRrujVSSRjod7m0oXFErUHqPsv7stZP6nD+5ioSi9jSOPb6t7ILnUlFXcW+xd\n" +
                "aVS7EyW8fYdhKYBYiodvyGpgADsLEICcsqxt2cai8s9KOrJHVBonpI1FyfL2BPV4+kxpW9zZQjHh\n" +
                "AhmNdMCko9g1WDRNeGgOfKv4jJx5Kml2NsmPUvc5X9MvdGpWLPrtYgv/AKEyoTJY2S2AiZPcoh8h\n" +
                "YBkoYU7OnolvNnLZ29EqxSflhHQAdxhHL1T1SUb45OZxruXKXjcycn4NRD1Nchqsm2xU7A0i9wi9\n" +
                "hQCJRQ4sixp7gaMT+pV37AJbBDT1RsljW0n4YnyFK32O5O4p+ThOnHmhHCtckmixEddj14lNcxPP\n" +
                "s9CXVxncIwbT23OGcKdEqxWLJpNZyjkW5zaWPU17mca1slGCszc9UttxRhLI/COn0443UVs+Bhqc\n" +
                "WLfVLdm1ExLTIhUIqxASG5WwbAT5Mk1e9mzairZlpcsby+HTQUlTlybbUc8l4BNtU7+CNRpdPZ7F\n" +
                "a6M6b2ey8FcIguNtan8FFYq2xvia/wAkUzUZOwEAQwsVgBQmxWJgF7iwNRhLU6psV7kTXLW/sSrG\n" +
                "r6nHwmx+o3G0c0o2lwmzq6eFYHe+5LGvrmlOc5bydeEOMorhGrwtO4jjjfcGLxu0YvHeZp8Lc6I0\n" +
                "iJ16m/eJIHbbdqiWOtMUmS2aiX0mSxtktlZDMynwyApjEAUz0ekVdPH33PNZ6uJVhgvYJVAABHmO\n" +
                "/wBrFr8oYmrNINuUHJDi0CZRa5CPAJ7BHgAYJ7ie7EuQNk9hMSYNgEntfgbF2FHivADLxQU27W6I\n" +
                "Nem3yNeUERLFpyprhk5Y/VdcnXOFoy+macJOpeGLCX643sCjOabSpGzxaXcot12LU0ov6GiSLaiG\n" +
                "NxW5TmskVSa0kOcn9NUu5a2RQ1wMnh0O9jCmBn6qfCE8llxGjddyXMzuwZcNOUr5Kw5VDHNSV29j\n" +
                "KQYIqWWn4KjSl+HxYKDvk0ljpf8AF7P2MmsmPaf4MWNyrpRW7Et3fYlXJ7m0Y29+O5lpeLSsqyTa\n" +
                "jGC5ZnOSjmmv2t2h5Ia1pXHYxt65KXKZ0kc7W634EZxl4KUvIsNUAWBlQJjsUmBFGmNLv3I7FcQI\n" +
                "sX6UE7KjOKjyjCWTu+DOS1SuGyJjeulZIyf0t2W2cuOenlotZdbqiGtbMtd5kE50jPDvl+C4lreT\n" +
                "shlS2kyJFZS2SxsllQm9iRyZIVVgIAqkrkl7nr8L4PL6das8F7nqBKAAAjy7VhYtNkNG2WlruJoh\n" +
                "WUmFF/S/ga4InafsyrAG96Ersl8lII0QEKXlFWnwFMOJ/Igl9t+AKKwvTmi/cnlbAnTvwEd7Rlkx\n" +
                "xmqkrNuUmS0VGShTW9omULNZAwOPqo6YRadb0xJ3FM36uN9O/bc5Yv6SVWl2kTkdQflgmZZZrUl4\n" +
                "IpQfY0Mr3NEaQwvYBd/kBxg8k1GPLOnF0jx5Yz9ROvYy6X/cL4Z2mOrjUg0J7WYtOKa1RyRXMX2H\n" +
                "l9VuHpNLfeyMXp68igpXe9/JNU4443dOvBorkqUdKRo41uVXfsaxnUwx13r4ODNt1Mz1FxseZ1Kr\n" +
                "qX8Fgjh8lWR+QKjRSoqMrdGVgpfUhfBvuTIqyJM5qlmq2SMm7ZqFYyWiVSVwlwwcIrerNpJTjpfB\n" +
                "hreB1NWuzI1KIuMttBp24oj/AFEeUiZdTtshYaMlLuX06fNbvhGeHHPPO1x5PSxYo4o+65bCamWC\n" +
                "8bf7zkZp1HVvJ9GN1Hu/JiuDWJpMlsbJYEsBPkCNGMQIDp6FX1K9kz0jg/Tl9c5ex3BkAAAeT3G2\n" +
                "T6ke6opOMuGbRIkt9hyVC4YRMudv7FWTJ3MYB3KSI7loBPYEygpACbKXuSltsxrkAhxXgonjJ8lA\n" +
                "d+J6sMH7DZn0rvDXhmrKiGBVbWJoDPLHVgmvY8+H2np1aa8o8yO23glURl9T9kZPd7jbqT+BXXug\n" +
                "FZrjla9zO0wi9MvYo2B8ATdAb9L/ALiPwzt5OXpMM1NZJLTGtr7nTLKo8HLr105iqfYmSpN9yX1H\n" +
                "hCjmk2lXckMdMlaCDrYfsS0dWFNd0eb1n+5/+p6V7bHm9Zv1O39IRiAdhJlDZN70Mzb+sDqjK4Im\n" +
                "XIsb+loUnuZU1vNI2Mce+Q2Iprd0h5YJR0SSbfPsPA2s0a7mmSNtshHnyxRRtg6Jz+rItMfHk3hj\n" +
                "cfrVN9rRqsye01pAqKUUlFUl4OLquo9R+njf0Ln3L6zPt6WN88tHIkakS1S292aRjW8tzOG8tuPJ\n" +
                "rZtClG1aMX4OnsRKGrt+TNiyufuBUsU4vi17EGGjQxAFeh+nL+HN+WdZh0Krp0/LbNwgAAIPJegi\n" +
                "S7o09PwLQzoyhS2pgxShTE3sECdybKXJETThAT3KJ7jAsZIwHY7JABT238GnO/kze48TuNd0B2dI\n" +
                "/uR0HJ0r/iteUdZYCie7HZLe4Q+x52Racs17npcRbPO6j+fP5FWOaf3CS9xv72UrAXK3ohqn7Gt+\n" +
                "xDTk0krb7AXGW253dNgpepNb/tT7EYOi0tSzVt+06XIx101IqUXJcmXoSb+pqivUb2E3k7K/yYbU\n" +
                "sUVzIeiC7mbjkfLSE8U3/wDIv7AdSkpcD+TPDGMU0t33L7nSOdHDPN6x/wDlS+Eenex5XUu+pn80\n" +
                "WIh8E2EnUSbKKbMr5KkzMI6MT2YN/URi2uwvczWo1w/c/g2McPdmjdJsgqM9GSL9zvUE93ujypOo\n" +
                "ts9Do1khgisrtvheEXASTjLbhGWaWmDk/hHU4W/lnn9Xk15aj9sdkSQrn+eQfhc9xt9lywSo2yuF\n" +
                "JFckodlVTlpRlKbbByXcWuD7EC1PyF6tpf3E3/xFfsQDTTpgP7l7oS3kkZrb1+nWnp4L2NBJUkvC\n" +
                "GQIAAg8j14+KH6qYJJ8RV+5nOMl+1L8HRlcp2jKTJTa5K5YDiW+CIlBANCACkMQAMAAAFF6cns9h\n" +
                "9iZoDqwPTnj7nb8HnYJW8cvc9AsQNES2NLVbmct0ASmvT3POnK5Sl5Z1579CdeDhntAKhFUJNJFL\n" +
                "f4CBb7I9Lo+lWGKnP+Y/8HDGtcV7nrslVjN1NozZpmW2pcox1nKukNbMU8klwmS5CeaMdm1YUnkm\n" +
                "+w4LJOSXYUJerJqG7o6sUZxX1P8ACLIlq4QUI0v8lC77bje50jBWeRklqyTfls9We0W/Y8dFQTfB\n" +
                "NhJ7isIUmCdCfIwq4Pe2yuxKu9hslG2L7Ry4S9xQ+1fAsjokVp0+H/UZaf2R3fueo/t2Mekx+lgi\n" +
                "mt3uzZ8ijHqsnpYW19z2R5be1nR1mX1M1LiOxzcy9kWJVRVfLHQJhZQyW/IrDZhBUX3BpeBNIK8M\n" +
                "Ke3YTQb9wAndMvFG+oglw2J7m3RxvqYJ9rZmrHpvkAYGGiAAA8epJ7FfeqezC2xbrudGGM01IEPI\n" +
                "qn8iQFIYkMBgABVAJDCABgAgbBioAxS05K87nrcnjvbddj1sb14oPyiwUKS+ka5Jk7lS4RUY9StO\n" +
                "KT87HBNXJJHV1UryV7nG39TILSivApSJsluwKT3TPYc7jZ4y4O3DleSNPlEqt3kd7GUoqTtbFSnp\n" +
                "OTN1Mm9MNl5RnF0smWcZOOya8GLb1W3bEJmsTXodD98vg7m6Rw/p2+p/B1ydsDRO0u3wP+5KoewG\n" +
                "Wd6cE3fCPJXB6XXSrpZe+x5j+0CQATKEUiUOwLj5GxLS/wB1Da3SFG/ENvGwYIer1EIPdLdik6id\n" +
                "H6bG3kyf/VGVegmiM+T08Mpd+EVexxdfkeuOPwrYHE3t7sa2VErm/wADs0hibBsgCrDcm90VYBpf\n" +
                "kGqFqC1LvQDGmiXBipgW9vgvDKs0GubMozrZ8G2GP8aHjUSj1XyAPkDDYAAIPI28g6+QBnRhlk+5\n" +
                "fAisnKEADEMBgAAMYIYCGIaCgQwYRLPQ6N/+NA4OUeh0q09PBfksKvuK92aX4RnJ03YRwZZas0/k\n" +
                "5pfczearJNe5zz+9hRYCAIdj1NcbCEFNyk+7EAAMTGDCO7oP5cn7nXTSOboFWC33Z1pMimh9w/wI\n" +
                "g4/1FpYopd5HA+Dr/UJXOEfCs43yaACTlsgKx/cBmUtPcVblxigH9HZ/3QRS1KmmDVcbMUHc9XcC\n" +
                "5y2O/wDTdumvzJnnTfY9DoN+krupMhHbwvY8fPl15Zz8vY9LPkePpZvvVHkS5SJFqlxQmFgzSDkl\n" +
                "gJgDfBS3IYOVcAbUlyJuHiyIw1K2yqjDlgNN9i01+6jF5XxFE03y7A2c8Zp0s4LPCnas5tKO/oul\n" +
                "jazNpr9qRKrv7gIDm0YAAHj6l3K2a2YNPwLTfY6MJyJqKuiC8lRpGYDGhAFUCEMBjEMIAAQFCBMY\n" +
                "C9l3PTxqoRXhHnYlqzQXvuemvKKUu5lmdWzVGOeUY4t+XwEcN/dJnNyzTJLZIiPJFFMEtjSiZ7Kh\n" +
                "ogAGAAA0VAJ8DBLU0vIHqdNHThgvY6OERBUkvBbpvky0EkwbHfgnvQHmda76lrwkc/c16l31GR+5\n" +
                "kaQBF1YBFW7AqMS67jiKTAmTFj4bJk+xS2gAr3PS/S98U/aR5p6X6Wqwzl5kSkP9SdYox/qZ5q3b\n" +
                "Z2/qc/4ij4icUdoiFMAEUDENiATYJWwfALj3AqU62iTpct2OMa3ZVNkC2QbsrQkt3QnOK4VgCgz0\n" +
                "OghOMJSf2vg8+5SPS6GTeDS/2vYlWOkBDMNGAgA8iMppf/sbyN7IT370hpxjwrOjDOabab5J38Gs\n" +
                "46lbJUaWzoCQG/hMVJ8OgBFE8DIqkhiQyoQDEAFIgLA6OmhqyN+EdabWxh0iqDl5Z08gHEWzg6p6\n" +
                "lD/id029NM87qdp12A55v6gh9yB7t0OCetWBpRjLeTN/c527bZIoQ+RDNIBiAIovpo6uoivG5mdf\n" +
                "QQ++f4Isd64Gokr3TLXs/wC5FFbE/uKfGz3E01e3YDxsjvJN+Wyewct/IzSF7GkVSJxxcpbJv4Ne\n" +
                "FQEul5JbTWzKbMpc8ALuV+1BppA+xAHp/p/+0/8Aszyz0uhlp6RN8JtikcfWz19TPxdGYr1Nyfdg\n" +
                "AwAChANiATGr7IHwLU48AVpa9xrX2ROudFKevZ7EC095v8D1RXCDQFexQam/ZHofp/8AJl8nnnd+\n" +
                "n3U/GxmrHYAAYaMBWAR5KTKqzNTZSnZ0QSdIExyi3BmEZtBG9EuL7EerfsP1F5ATe40JbjRFUhiQ\n" +
                "wBiHYioQBW5cI65xj7gdmJaMUImq3Iq+BrZATmnXB5+TJrfwdPUZFGLZxJ933AkuF3bJLgQOcqiz\n" +
                "E0ybRM0WKBgAQDEMqA7+lqOJR3t7nAuS4OXaTS9jNaj1lfe0PWvc4YdTOKppS+RvqcksbfG+yJq4\n" +
                "63NJ8mWXqoKL+tp1sjilOU3vIzcJODkovStmxKYmEXOSjFW2z0IfpySXqTfukV0GOEMSyUnN9/Bu\n" +
                "sjnKorZcsXpZy0w4seJJY1SMOu6fXB5IL61z7o216WgnKvAlSx4jYRV/Uysii8rUOGyntsi2splw\n" +
                "SypdkS+RCkzv6NqXQZo90mcJ0/p9+tOK4lFlHMuBiS2HRQAMQAIdiAGSyilGmkAQiqTTHJKXsDko\n" +
                "y8P/ALG5AR9UfgepMV0xOnxsyCmdv6dJ3OL+TgSmuDt6K/W47bkqu4AAwoHYgA8nTfBOloSlRopJ\n" +
                "8m0KM6VPgznFJ2uGaOMfJE6pUUTQq3GCCKGhDRFUgEhgAAwCBG/Sq5yk+yMDswR0YU3s3uFaKhZZ\n" +
                "KMedhTywxq5SS9jzuoz+tLa1HwAs+X1JbfauCV9qJKfCKgs0x8P5MzWP0wslVlkdz9kSHcZUAAAQ\n" +
                "wEBQcblxmvBHYEyWLrrUIyjccmP8uiJzrGo2m77MwYfgn5X9KjqnJRjy9j04Yk+m9JOtqb9zzMcn\n" +
                "DLGS7M9HU71Q3Ut+TPXxrn6iOGeH6YytHQqx46Rk3kfKr8g9SVyfBnW05Jsw6zqXJ+nB7fufkjPm\n" +
                "adLk5jfMc+q1hHS7G2Ml8hA92R3L/c/ggsSjueh0GGTccvEV/k89cnpfp2b6Hil+0tI4ci0zmvDZ\n" +
                "B0ddHT1MmuJKznAYAgKHEGgjFvfgpqqYCUKTHSkvcd8+5mwKk21UlwRdbINbvcNnuQD4EAAG6O/9\n" +
                "Ob+tV+ThirdJW2etgxLDiUf3dyVY1EAGFAxAUePsHIDNMjYU+EXCLnKoq2X1GJYsUVzK9xq45wQD\n" +
                "QDQyRgUNCQAMTHYUAmZSk5N6pN+C5+DMqE23u3bEMQU+5044ReNWtzmR1Y/sRmrEOCtiyuoJGqVm\n" +
                "Gf70vCEKzGIZpkAABAAAFIYA+AGOhBYA1R04MqlGnOn48nMLdO0SzVlx6F/8mzLLmUVzb8HPLNkk\n" +
                "qcmZknLV6Nu3b5EAM2w6OyJ7jj9qBcmFT/UyC/2SZmWJR3NYSkmnj2nfYx7nR07vKml9XYVqN54s\n" +
                "3UJaoqLXdvk58nT5MT+uO3lHpQjpW7tnN1EnKdXsjMq2OSMGylFWU9iWzowqTSYOSe6FJ7IzapgN\n" +
                "y8CbRNgQMQgABpNulyI6Okin1EF7ko6uj6WWOXqZFv2R1FPdiMNEAAAwEBVeXHFOXavk2h0yX3uz\n" +
                "ZNUPV7k1qchJRVRSRxdXPVkUfB16/J5+SWrLJ+45TrxJQhm2AMAAYxIYAFgTPbYCW7JGxFQmIbBA\n" +
                "Ujqitkkc0N5I7Iql8ma1BpRnnw6o6lyv8m1ASDzgLyx05ZJdmSbZAAAADAAABgUAAABsJjB0QIAA\n" +
                "AENiKNoP6ENCxxfp/kbVIw0l/wAp/Jmay2xGRqM0u56uGMIY1oS37+Tyjv6R3h3ffYz03y6tRx5n\n" +
                "9bN3J9kYZ13MctdMWxcoTZJ1cjewaibF3AbYgCwAVgFANHR0brqoGCRphbWaD9yD2AFe7T5GYbAA\n" +
                "ABQBQBHAm6C9/cF4FFr1H7bEdDl9MG/COK73OrPOoV5Obsb5Y6CGJcDKyBiGgGMQANGbdsqXHyTw\n" +
                "AmAAEPT9Dl70SjqjjXoU+5zzg4S0sKvArn8HUY4FUb8m1masMO4Jg3yRXBN6skn5YgezpgdGAAAA\n" +
                "AA6ACWyiscNep9ooDOx7CQAVsGwgKABi3QAzbDh1fVP7ey8mDZ6EftXwZtWJcVXgicaRqyMnYyrL\n" +
                "Iv4ZgdWSP0NV2OQ1EoLhklidxZA2UegpqSuLFJJr5OLFP0532NZ9R/Qjn+W/0jJHTJogTk3K2Vty\n" +
                "dHOlp87A3p9mJybVdhUAN2CQ1EoKmh0MAhbjTAAPS6Xqo5voybZFw/JvKWhfVG/c87NgahHLj7q/\n" +
                "g6Ok6xZUsWZ1Ls/Jlp0LPj8M0VUYZsNLZd+xsuEZUwoEOwPLctKbZOPi33Ms01LaO68iWWenSqXv\n" +
                "3Li6rJLVP2RL4JC2aYqkAIdAIYhgMARMpdkApS32JQwACoq5JeSTXCrk34COh8UTlgp0F8AzLQqt\n" +
                "hivcpMBIoAA5M0ayyINOo/nP4MzcZIBgAhgAAzq6eFYv/Y5orVNR8neklsuEZtWPOqnQF5o6c017\n" +
                "kGogGICgAYgBK5I7zjwq80fk7LMVYRMt5ooXMyKb+/8AByZcbxy/4vg6/wBzM80dWOvfYSlci5GP\n" +
                "TT3EbZIqPIBH7gBxJo1aJrcBKIx0ACAAAAAAAEAAen0rvp4X4o5eq6VxfqY+O68G/RO8FeGdBhpy\n" +
                "9H1qa9PP+JHY1p90eb1XTqMnLGtu6L6PrXCseTeP/RfR6AxdrjvFgZV4QwA2yaBoAtgJOitflEsA\n" +
                "LTQNpEbip8gNzb9kJCGAwAABHRi2h8mCVtLydHBKRXcUheCmrZlUFIekekoSGmGkNIVzZv5rMy8v\n" +
                "82RJuMAAAAABAbdKrzX4R1nP0q5Z0GK1HJ1SrL8oxOjq/uh8HOajNAABQwAQGvTr+L8I6WY9Ivuf\n" +
                "4N2jFaiO44/exihtYDXf5E1c14Q4v6QIrDPGMUmlu3uYm/U/bH5Oc3GKYCY0yi0JrcFsMBIAGAhD\n" +
                "YgAAAADuAAdnQPacfydZwdE6yyXlHdZitRK/mnL1XS1eTHx3R0x+9mhIrh6Tq3iemW8PB6cVHJFS\n" +
                "hLZnndV0u3qYl8o545pQVW1+TXrLNDEBUMAAAYhgFIJMfCsnuECBAMAAACtMMblfg2aDBGsV+TTS\n" +
                "ZqxnW6NEhJdyo8EU0h0NDIJoKKCgODqFWaRma9V/PkZHSMgAAqATGCVyQHX0y+hs1ZOBVj/JZzbc\n" +
                "vWcw+DnOnrP2nMbjNAABUAADA6+mVYU/LNLIx7Y4r2Bsx/WlN7Er7Qb2HX0IgOwwYBWPUfZH5MDo\n" +
                "6j+V+TnNxmkIYLkqKQ0IewAAAACGIAAAAAAYGnTus8X5O+zzYOpxfuegY6aiocsomBfJFCMcnSY5\n" +
                "zcuLNhgeMAAbZAAAQwAApSEDe4BAMAChD5dCNumx6slviII6oxqKXjYdDoHwzm0n9g4rYJ/ahxQD\n" +
                "odDAKVAMO4HndS76iZmXnkpZptcWQjpPHMAAWUBWNfdLxsQdEo+njjF8vdktWOnEqxxKCCqEV7DM\n" +
                "NOXrF9MGcp29Wv4F+GcKNxmmAAVAHLS8gXhV5oL3A63GvwS0dDREonNti+C3whNboLCCwsVgFTm3\n" +
                "xM5jqybwl8HKajNIFyNi7mkMfwIaAL8hqXkBMB2gJYqAsLIGgNo4sk46oxtD/wBPl8V+Tr6ZV08f\n" +
                "7lsxrWOSPSy/dJL4OoBE1VItMzRSINBAmMo8YBDNsgAAAH2EEuKCJGABRY7EADR34FWGPvucCZ6O\n" +
                "NViivYz0sUEuBoT7L3MtJydkUiZ/ckUgKAAADPNP08Upd62NDl65/RCPlliVxoYhnRgAAAbdJGM8\n" +
                "31dlaRp1G+T8h0Ud5T/CB/V1EF5Zi+tR1iGBlpl1EHPBJLnk809c8rI9WSTSq2b5ZpAFgaZBt0qv\n" +
                "qIGJv0f+4XwxSO5oTKZLOboyfJFGiVzDTuwjNoKNHEWkCGrizkO7TucUlU2vc1GaQu4AaRQAHYAE\n" +
                "MAExDEAilwIpK2kSrHp41WOC8IbGDRzbQIolgNDQhgWh2ShlR5FgIZtkAAIIaJbtjfBIUFCQwFQw\n" +
                "ACoLVOKXdnpPbszi6WvV1PiJ2+rHyZqwcCf3L2K1J9yVWp14MqmX8wtAsSnK3fymW8PjJL8hUgP0\n" +
                "p/1p/KJ0ZV/Q/wAgM4uuleSK8I7Ky/8A47+Gednblmm2u/BrlmswQAbZAAIK9DpdumT8tsyxfV1c\n" +
                "fZG0bj0q230mfTRfqOT5owrqASAy0HtFv2PJPUyOsU37Hlm+WaBiGqNMg26P/cR+GYm3Rr/yF8Ml\n" +
                "I9AmQyZHN0Tj+6TKiufkMX2tjj9qABDACWjhzqs017noUcfVxrLflGozXOAAbZPuMAAAATABAAB3\n" +
                "NcSvNBf8jOPJ0dMr6iPtuZrUd75EMDDSWiWi2SAIAGECKJQyjyQBxdhubZAxUx+wCZI3yAANANAK\n" +
                "h0MfuB39HjS6ZN8t2avFF9kGGOnBBexTOdaZvBDwSsMot0/7m6AKUFUShDAAAAgT7+DxZvVNvyz2\n" +
                "X9svg8Q1ylMAA2yAS1SS8uhl9NHV1GNe5Fj0HgVJKcqXuJdPJbwyVflG7Aw0w9LKu8H/AIFWVc47\n" +
                "+JHSBNHF1E5LBJPHKN92eeer+oP/AMR/+yPKN8s0AgA0gOnof5z/APU5zp6GDnklplpqPJKsdtGc\n" +
                "9i3iyricH8ozyQyrmMa8pnNtcNsRVUkJL+GkNtWABQrXkdgIw6uGrDq7xdnQDSkmpLZ7MsR5IFZI\n" +
                "enklB9mSdGD8AHZAAAAAIO4xMCocnV0a/iSfhHLj5Z29GtpMx01HSAAZaJiKEAgAAhoAGFU+lxsh\n" +
                "9FBmqzQfgpTh5ZplzPoImcugfZndqXaTHf8AyX9gPLl0E/8A+RD6PIj2N/YN/wCn/JUeK+nmuwvS\n" +
                "muYs9ql3iLRDwB4unyXih6mSMfLPWeLG+yF/p4cpAIRXovtJi9OS/cZxrSANGT2f4Cp/0omAGK3/\n" +
                "AEMWr2f9gqhC1R8odryv7gHKa8niyVNrwe03UW/B47Tbbfc1yzU8gOtwNsg6f0+N5pS/pRzHf0Ea\n" +
                "wylX3MlWOoZJRzbAAAHP16vpJezR5VHrdb/tJ/g8s3z4xS7gCA0gO79MW+V+yRwno/pqrBN+ZEvi\n" +
                "x2E5PsZQNWqObbmlNUnfcayRfhmvpR8IbxQfZAZqUR3HwHoR8C9Dw3/cCtMQUFfL/uS8M1+8WjKn\n" +
                "ymVHmZZa8spPyQypWm0+U9yTowa4GJcDAQAAAACsDSC+mzt6Rfwr8s44fYd/TqsEf7mK3GohgZUh\n" +
                "DABAMQAMBgc3pY39uQPSmvtkn+TmfJriNDSsy4/7D1c0eU/7G0S1wEYf6nIuYlLq/KZozlzgdP8A\n" +
                "q0XHqovujzo8FxA9BZ4vwUskWeZ3NI9hpj0FOPn/ACPUv6mcyKRUdF/8l/YLfsYopBGn4DbwJFjF\n" +
                "RUWL04PsjRmUuRhoeGLRm+kxvsaR5NOwHHLoYMh9B4Z3sQR5suhl2Z0Y4vFjjDS9kdZLJfqxhr33\n" +
                "tfgeqL7mhEjLQteRmEu48XIE9f8A7V/KPNR6fW/7b8o4Hwb58YrMBsRpCo9bpYen00F53PLXJ7Mf\n" +
                "tX/qjPSwxiGYbAAg7gIYDAQ06EKX2y+GEeNJ65zfltkUUuRHVgIBDQAIfcTABPkAfIF4n9VeT1IK\n" +
                "scV4R5UPvXyesuEY6ahgAGWgAAAgACgGJDA//9k=";
        String laureatquatre = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDADUlKC8oITUvKy88OTU/UIVXUElJUKN1e2GFwarLyL6q\n" +
                "urfV8P//1eL/5re6////////////zv//////////////2wBDATk8PFBGUJ1XV53/3Lrc////////\n" +
                "////////////////////////////////////////////////////////////wAARCAHcAdwDASIA\n" +
                "AhEBAxEB/8QAGQABAQEBAQEAAAAAAAAAAAAAAAECAwQF/8QALxABAQACAQQBAwMEAQQDAAAAAAEC\n" +
                "EQMEEiExQSIyNBNRYQUzQnEkFFKBkRUjof/EABYBAQEBAAAAAAAAAAAAAAAAAAABAv/EABYRAQEB\n" +
                "AAAAAAAAAAAAAAAAAAABEf/aAAwDAQACEQMRAD8A9u1QRRdiAbBQRYAAggu0BRdm0PXsFRjLlmLl\n" +
                "nzZWfTAeg28ffyZfTMnt4MLhhJld1UT6r/iaz/7Y7AOP1f8Aa1jLfboBrn2U1XRA1zG7jtjWkUAA\n" +
                "ARQBQAQEVANgAEAA2ICgKAIguzaAi7NooobABQBAKAbCiEvl8rrfycn1Y+T135WSj64gCiAAAC/C\n" +
                "AAAgQm7dNSaXBJjXPn8TzdO2/DHJx456uRg8m/2m1x4+TP8Ax1Hrwwwxn04tT2Dlw9POP6rd132h\n" +
                "4UXZtNkoKGxBUAFSzYoOVmh0sc/VRQAUCAAggqCAoigJQUAEFgAIAAAACgAAHwAAChUXaIix8nrf\n" +
                "ycn1o+T1v5OSj66QiggACiAIqZZTCbohbqbpj9Xp5+/Ll5dT7Xol1NXx/K4NfxfBbqeWe749/wAk\n" +
                "mvdUa8/Hoxmp7Z7tJ3KOncnc59x3A6XJO5z2bBu5Hf4YuSd0B0/Uq97l3G0HbvamW48tybw5NA9K\n" +
                "d2vbGOcsW2WeQb2zlPG0mXxpbfp8oMpEwy7tqyoQUVAFAAABBAAFRQAFBFRAAEX5QAURRQAAEAAE\n" +
                "J7fJ638nJ9aPk9b+Tko+vAAAAAAN6efly7rp15LdeHLHHzvJUXixmE7q3cv/AEzu618JtRuXRcnP\n" +
                "ZtRvuTuZ2mwa7jbOzYNbS5JtjltmG4g1eTyu3l3XXiy3Qddm0ICkTYDpjk3M3GNQHXe/lbZq69Oc\n" +
                "a8+Z8A58GX1WO/y8vH4zeplQCehQFESotRAAFAFABAigAioAAAAAKAgAgAAAKfMfJ638nJ9ae3yu\n" +
                "t/JyVH1hAFQADLxCMXOdwjd5MccNfLjcrf8ARnbb72z8NQXabEqips0oM9xK4clvfpMcrL7QehYz\n" +
                "FBdtY4Y8mNxrIDjycOWGWrjbG+PjuPmu36uWtWsW7BFgABF0AuNRQalXfnyzF+AY19b0T0zJOy35\n" +
                "XH7WaNCKjQUAQAQRQUAUAEBUAABAAUDQAIAoigAAAAT2+V1v5OT6s9vldb+TkqPrQQAABM7rHw5b\n" +
                "vz5a5cvhyWIqCKKGzahpZ7AGefp7l9eHn+HDDjzuXnGz/b1453H1TLO5IOcmlIAAABsAAkANuHPn\n" +
                "ZdSuffZ8g9e2pXPityw26QFnpYigvxW8PtYny6YfazVgAigICiAKIAoAAAAAgAqiKiAAIRUKC6AV\n" +
                "QAEVBEJ7fK678rJ9aPk9d+Vko+sAAAg4cv3smd3nRqIIppoYyyk9kzxvqsdRjqxynhB65RnDzjGl\n" +
                "FE8roACAMZ3tx22nJhcuK69oOH6uW3Tjz7nn3rxZqx14Mbu34B2i/JpVGefprnJlh5rjj0nLb9U1\n" +
                "HqxysW52/KDEw/TkxgoAsRYCz3W8Ptc57reH2s1Y0AgCAAAoAAAAQBFA+VUPgRAAEAFUARFQgAqK\n" +
                "KgAEfK678nJ9aPk9d+VkqPrAIB8UTK6xojze8shIrUBqMrGhvsx5MdZe/wB3DLpbL93h1npd7gM4\n" +
                "4zHGRdKAmnHn5bhlJHf5c+q4dyZ4+f8AQOWHPlv14ejCzKbeP+JP/wAevhwuOE2DWmsL2gCZcfHl\n" +
                "d3CbLJPXhTQMjWtGgQ0ugE0Lra6QSCpfYLPlrj+1i+muL7WasbBEUAAAUAAAEAAFE+QFEFABAEVQ\n" +
                "ARFAVQAASiC/MfJ678nJ9ae3yeu/JyVH1g+KAMctnY258/2A4bWMtRYgFume+fu0NyrEnmeFBQhB\n" +
                "FbxumYsBbq/4xNfusATRI1o0CaGtGoDOk03o0DnnlMMd15r1XnxHXrJf0ZY8UuoD3cHL+o66ebo8\n" +
                "Ld5a1HqFRmtWs+0D4bw+xhvH0zVigIAVKKbNooAAAAKAoAIAIoAAAICoqgAAAgACE9vldb+Tk+tP\n" +
                "b5PW/k5KPqgIHwxzf222eT+3VR5Z6alSeVkWDUndXm552Z6erC6q8/Djz47l1Wh5ulyt3Hoc+Lhv\n" +
                "Fvd26wQS5zG+WpNvN1W5yaB6MOTHL1XR82Z9l8PocV7pNg0sFAkW+JurI49bbhwbBL1GEy1t1wym\n" +
                "c3K+TNe3q6HO/rdvwmj2xrRZo2oupZqzccb0nD3d2vLtAEkxk7ZNRm4xtKDnZGNO1YqKw3iy1j6Z\n" +
                "pFRUGjYAhAgiqCKAioKIqgAACCACKAAAKCooHyHyIAALPb5PW/k5Pqz2+V1v5OSo+qRF3oBMvtps\n" +
                "+Kg8k8VtnKfVVaiLGpWVii+1RVRrH2nVcF5sZlh90XFuZaB4ePpOS5zunh7sMJh4iyrAXUXUSNQC\n" +
                "M83FObjuNaX0g+Xei5plqTw9fSdLeG92V3Xq2gCNJoEFFESxpKDNYrVZyRWWp6ZnpYzViooKRUEA\n" +
                "iCinyICgAqAgoQBClFAABUEFEAFiANIiqACIfL5XW/k5Pqvldb+Tko+ogIACjhyTWbMvl05p8uUW\n" +
                "Daxnfly5s7hVR6JfCxx6fkucu3ZUWNMxdg3i1GcWsfYLldTyYZy+rHg63mtzuMrz48uWHmWg+0Of\n" +
                "T59/Fja6ICgAADnzcmPDh3V4/wD5Hz9vg/qe94fs8e/APrcHNObHcdK8H9N33ZX4e6qrNYrWVc0B\n" +
                "qeks8LGasUERVKhQAFFQAFRUAAFABKAAAAAIAKIvwi7FUTYCgCK+T1v5OT6z5PW/k5A+mCICoKM8\n" +
                "k+hwj0Wbxrz/AMKjePtz6rivb3Nx033Y9t8qPL0n3WvV8GOOOM8QEWKka0ouLePtiNwHi6/gs5O+\n" +
                "S2X3pw4uHLlupjZH18bueTxPUQTiw/T45j+zRFgAAAAMc3DjzY6yeO/07LfjP6XvTYOfDw48GHbj\n" +
                "7+WqrNFYrFbrNBfgh/jBmqGwFAEAAARQUQUFiKCh8CCCL8qAAgi0FRQQAFQABQAWPk9b+Tk+rHyu\n" +
                "t/JyEfSBBV2IoHw8+XjJ3cuX3FRJ6WVJSA6T0s9MxqemgjSRQaixmNwRcWkxigqpFQAUEAQEUUZZ\n" +
                "rbNVXOsujF9gigzVgCoqAQEBQPkPkABQAAUQAFRAFQEURRUURARVEWewBRFAnw+T1v5OT60fK6z8\n" +
                "nIR9EAUiooDnyzeLol8yg889NRJPNWKjUajMaijUWIsVFjpGI3iDUWJFgLoIqAAAigIADJVSqrFc\n" +
                "77dKxQCAzVgfBsRUUARflAFIgCxWVABQRUX4AAQD5ARAFUCkEURQQAFioAR8rrfycn1fl8rrfych\n" +
                "H0hIbFURQAQHLOayJ6a5Z4lZnpYjUaxZjUaFajLUmxGo3ixjG4DcJEla+ECKk9KgAAqAAmlARmtM\n" +
                "2KrFYvtu+GPlRFBmqgCAAKAAAAAAoigKggqAAAoIoiACqACAAKIoD5XW/k5Pqz2+Z1k31GQj3gCg\n" +
                "AAAJyTeLlL4jtfVcbPKo3Gp6YjpPEUXGNY+2ZWpFRueVn7/KYtyAT3tohPSAoIKJs2C6Q2AJpQGU\n" +
                "rVZsVWazYtTaiVn5arPyzVAEU+AAAEEUgoAALE2CNCABs9oKKgIAAoAAm1BRABpme1+QJ7fN6r+/\n" +
                "k+lj7fO6r+/kg9oaGgAQAAHLPxk7Rz5J6VDFtjH03FFxnh0x18udyk9LN2qjrMosyrEsheSQHWDh\n" +
                "eb+WbzT9wejuh3x5MufFn/qMUHs74nd/Lx/r4n6+P7or1938rMv5eP8AXizmn7qPZ3ftTurz480/\n" +
                "duc0B27/ANzcrnMpSz5lBayd+vGS3V9ATyz/AJV0wnlP0rct7RXMXPG4VlAAFBNiIoKCAAoCilIb\n" +
                "BABQVAF+UUQE+QAAFABZ7Ei/IE9vm9V/fyfSnt83qv7+Qj3IAoQAUBQZz9NM5ehEx9Hczb8M5ckk\n" +
                "UdZlJC56ePLm9ueXLaD159RMXK9Vt5bls0iO2XPlan6tco1PINXkqd9NGhTvtTdakKDPdU76tZQb\n" +
                "nLWpzeXLQD14dTqO2HUSvnrMrF0fTvNjTjz1Xzf1LLt24+W2+VH08PN27T08PF1Eni17cL3YyoOX\n" +
                "UfDk69R8OW0VABQARQEFSgCiKoRUAFQBU+QAAAAAABQAURVCe3zuq/v5Poz2+X1lv/U5IPeqAAAC\n" +
                "kZyy7ZtRrTnzZduPhzz6iT08+fNc5qiN58unHLO2sZW2oC+0UQRrHyla4/NBrtmk01pAbmMO1cPM\n" +
                "a0DDOTro7QcDTdkYorNRqsoNQSe1ES+Elq1FGt17v6f1N7uzOvn703x5duUs/dR9vmx3jt53bHLu\n" +
                "4Mb/AA4oAA0AREFiAKIoCoqgAobEVAKAAKCAAEAFE2qhvysZX5BY+X1k/wCRk+pHy+s/IyEe8AUA\n" +
                "AcOrz7cNfu7vH1t+uIPNUi7BA0AIs9pq7axl2C2eGcL5dpJY5zC93gG2a3pm47B04vTppjjmo6Iq\n" +
                "JZ4aSqOdYvtvkymMcbyCLYnaz+pTvoNG5tjuTZg2ibAWLj9yRcfaj7PHf+Ni5tYfjYslABlQFBAF\n" +
                "BYgCgoqCgifKoAoigAAAAAAAKLAgCx83qv7+T6UfL6y/8jIR7oENooABXh6vzk9teHqd94OJtAFa\n" +
                "kZi7uhGtz5P1JPhztRR1nIuHJ5cpLq3Xgn7g9Pwk8HHe7FUG8HRzw9ugJUqpVV5ue+XF2z13+WLh\n" +
                "7uxDs+ju2w1Mcr4kqWWewJN107ZIccM6DPZ+yempRBlrDzlEdOCf/ZP9rB9Wf2cYw6Y6up+znfYC\n" +
                "CooHyAAAKiii1NqACCKBANAAigCKIgsAUAFCKQBqe3zOrn/IyfTnt83qv7+QPUAgAAV4+snl7Hl6\n" +
                "yeJQeRUiyAsbwx37Y+Fwy1RGc5rJl1yx7ruOdx0o1OSzjuHxVwxwuFuWXliTbU46DXFfNdtOfHh2\n" +
                "uvwguPhrbnFFa2fDO61PMB5uSXuTHDJ6e2WtzGA80vJjNRnsvvLy9OUkc8paqOdy0xd2t5RlFJ4F\n" +
                "1s0COnTTfLHOvR0eG89rEfS4p9V/05X3Xbi8d1/hw3dgoCNIoAACCoqhFSAKAgnyqfKoACgAAIoI\n" +
                "qfKgAKLFSALHzeq/v5PpyPldb+TkI9uxBFVABXn6v+27uPUY92APDGozZpRGpJpNGLeOtAYWumOM\n" +
                "vsxxjpjjIBjx4r2yNArFxZy8R0c+TxAZxajGPpqXwCrjU2mN8g6yNSJC5aBazZCVfAOOcY065M6B\n" +
                "jWvbNdMtuevOlRMMblX0em4+zHbz9Ph9Xp7PXgg64XXFnXmx9O3Nezpf9uPH9sBpUVFBFBFRQFQV\n" +
                "VVme2kEIfJBBU+VQANqAbEEFpARYaFAFUIsnkxx3XbDCQRMcPl8f+ofl5vtvif1D8vMHrAZUABDL\n" +
                "7fI5c+Xbj7Uefmx3uz4cY9HT/X3SscvHZ6VGI6SeHKV0xoN4usvhxldJl4Qb2dzO9m/3FalY5fSy\n" +
                "wy+qA447nt0lmmbNM7oOlu0k1Wca6fINz7UTxE8A1KbSUELUC0EqYY7yNbr0cHHdrB14Z2RvHdzi\n" +
                "XUjp0+Pm5fCjj/Uc9TDCfumH2x5+qzmfV3z4j0YX6UGgEUBQRT5AAAIqRQAAPlUAAUEWp8lAABQW\n" +
                "TYGmpjbWscP3dJ4VEmOmtifIivi/1D8vN9p8X+ofl5ivUgMqCbNgu3j6jLeT1Z3w8PJ5yu1Rrgy7\n" +
                "OT/b0ZTf/l5PXp6ML38c8+Yo5Z4apG7P/bFmqDRsgg1jWtufklB1mi39mNm/AGV3GFTQptZTtJiD\n" +
                "cVj0sEa2JpVFZvmr7bwwtsQXi49168J2RMMJjPLOV3WoL92TfPnOn6a35pwYf5Zeng/qHP8Aq8vb\n" +
                "L9MB5sbvPuvzX0eK/Q+dh4r6HFfpQjoAKRUggoAIsRQUBRUD5QAAFgAJVKCB8tzHdETGbdccNLjj\n" +
                "qNVQEi7VA2ICvjdf+Xm+y+L/AFD8vNB6vhD4GWg8DHJyTCe1ROXLTx5ebW8uS5OYEdODk7MtfFc2\n" +
                "VHs5MfmOGXiunT8ks7cjl4/Pj0DnMm5XPXbWpQbZs8ndWpdgk2qwgJIulGQkXQorOlNm1QtTaV0w\n" +
                "x2ouGFtejHGYz2zjrGFy2Qayz+NrxYfqZa+GMcbndR05+XHpuPx9yjPXdROHj/Tx918v+TkzvJn3\n" +
                "ZVEHTi85Pfxz6Xg4fufQw+0IqgKEBAVAFAUIqRQAEAABUWGAa3VdMcN+bDEZxw8us8HoaQ2CVRUA\n" +
                "FAQV8X+ofl5vtR8br/y8wejQu3Hnz7Yy0vJyTGajyZZbvlMrbWRGmUVQQAJdXw9HHy92PbXnIDvn\n" +
                "j8xiJM/GqSg1GkxaBJWpUMQXu8GxEGtm2VBSJJa6YzQGOLpLpMZtbPChtrGXOyHHx3K/wvNz4cE1\n" +
                "PNUdeTlw6bj9+XyuXly5ct5VOTky5ct5MICxFgPR02Mte7H08HT3T3Y3wCgqKiwIAoAioKKqRQUA\n" +
                "EFSIE9tybMcLfLtJIqJMZPbWyooG0tQRdoCigAqsqCx8br/y832Y+N1/5eaDtlXk58t5aejkzmMe\n" +
                "PO7u0VKIoAAIAACyAipRRrHJuZOSy6QdZWpXPHKLufuDe02z/wCU2De2sZtJja1jhlsG8dRbSY+f\n" +
                "NW58WE97BcJb8NyYYec68+fVSfZHnz5Ms75qj1c3WTXbx+HjyyuV3aggKAgigLjlr093T8ndi+e7\n" +
                "9NlrIV9BUl3BFUAADYCooLAgoCkltAjphhv21hhr3Gv9CE8eIJ520qIWlrIACgAqCoqKKioK+N1/\n" +
                "5eb7O3xf6h+XmCc1rz1vK2+6zUVlYAAgAAB8tRkBQFRAEUGolBAAbx5MsfS/rZ/u5qotzyvusgAK\n" +
                "gACAAAAA6cXjKObePx/sH08NXHwrHD9rYARUUE+VAVFUFRoRccba7zGY/wC0x8Yr8qFppPlfgQ2m\n" +
                "0pFAAAAAAFAA+V3o+GcfOSCz2+P1/wCXm+zHx+v/AC8xX//Z";
        Laureats.add(new Laureat(laureatun, "M'rabti Younes", "BROME","pas de description"));
        Laureats.add(new Laureat(laureatdeux, "Dodjo Akakpo", "ANCFCC","no description"));
        Laureats.add(new Laureat(laureattrois, "Sara Ainane", "GeoNet",""));
        Laureats.add(new Laureat(laureatquatre, "Harbass Ouliya", "MarMap","ing"));
        Laureats.add(new Laureat(laureatun, "M'rabti Younes", "BROME","pas de description"));
        Laureats.add(new Laureat(laureatdeux, "Dodjo Akakpo", "ANCFCC","no description"));
        Laureats.add(new Laureat(laureattrois, "Sara Ainane", "GeoNet",""));
        Laureats.add(new Laureat(laureatquatre, "Harbass Ouliya", "MarMap","ing"));
        Laureats.add(new Laureat(laureatun, "M'rabti Younes", "BROME","pas de description"));
        Laureats.add(new Laureat(laureatdeux, "Dodjo Akakpo", "ANCFCC","no description"));
        Laureats.add(new Laureat(laureattrois, "Sara Ainane", "GeoNet",""));
        Laureats.add(new Laureat(laureatquatre, "Harbass Ouliya", "MarMap","ing"));
        LaureatAdapter adaptateur = new LaureatAdapter(getContext(), Laureats);
        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialogFilter=new Dialog(getActivity());
        malist.setAdapter(adaptateur);
        malist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Laureat C ;
                        C= Laureats.get(position);
                        ShowPopup(1,1);
                    }
                }
        );
        FloatingActionButton filter_fab = root.findViewById(R.id.fab_filter_laureat);
        filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupfilter();
            }
        });
        findbyfiliere = root.findViewById(R.id.snipper_filtre_laureat_filiere);
        findbypromotion = root.findViewById(R.id.snipper_filtre_laureat_promotion);
        findbyprovince = root.findViewById(R.id.snipper_filtre_laureat_province);
        return root;
    }
    private void ShowPopup(double lon,double lat) {
        myDialog.setContentView(R.layout.laureat_show_position_pop_up);

        myDialog.show();
    }
    private void ShowPopupfilter() {
        dialogFilter.setContentView(R.layout.filter_pop_up_liste);
        dialogFilter.show();
        List filieres = new ArrayList();filieres.add("SIG");filieres.add("GC");filieres.add("GHEV");filieres.add("GE");
        List promotions = new ArrayList();promotions.add("2015");promotions.add("2016");promotions.add("2017");promotions.add("2018");promotions.add("2019");
        List provinces = new ArrayList();provinces.add("province1");provinces.add("province 2");provinces.add("province 3");provinces.add("province 4");
        /*ArrayAdapter filieresadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,filieres);filieresadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter promotionsadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,promotions);promotionsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter provincesadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,provinces);provincesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        findbyfiliere.setAdapter(filieresadapter);findbypromotion.setAdapter(promotionsadapter);findbyprovince.setAdapter(provincesadapter);*/
    }
}
