package com.example.adminappcall;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


// ViewHolder Personalizado para las fichas telefonicas incluyendo numero nombre y foto;
public class ViewHolderFicha extends RecyclerView.ViewHolder {

    public final ImageView foto;
    public final TextView numero;
    public final TextView nombre;

    public ViewHolderFicha(View itemView) {
        super(itemView);
        nombre=itemView.findViewById(R.id.Nombre);
        numero=itemView.findViewById(R.id.Numero);
        foto=itemView.findViewById(R.id.Foto);
    }
    public void onBind( Contacto mContacto) {

        if (mContacto.getNombre() != null) {
            nombre.setText(mContacto.getNombre());
        }

        if (mContacto.getNumero() != null) {
            numero.setText(String.valueOf(mContacto.getNumero()));
        }

        // llamada a la libreria picasso para cargar las fotos de perfil
        // en caso de error o ausencia cargara el icono de launcher redondo
        Picasso.get()
                .load(mContacto.getUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(foto);
    }

    public void onBind( Medicina mMedicina) {
            nombre.setText(mMedicina.getNombre()+": "+ mMedicina.getDosis()+"mG");
            numero.setText(mMedicina.getHorario()+" hasta: "+ mMedicina.getFinalMedicacion());

        // llamada a la libreria picasso para cargar las fotos de perfil
        // en caso de error o ausencia cargara el icono de launcher redondo
            Picasso.get()
                .load(mMedicina.getUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(foto);
    }
}