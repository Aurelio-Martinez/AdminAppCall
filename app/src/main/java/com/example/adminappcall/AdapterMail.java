package com.example.adminappcall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class AdapterMail extends RecyclerView.Adapter<ViewHolderFicha> {

    // lista de clase Contacto
    private final List<Mail> mMail;

    // Constructor con lista Contacto
    public AdapterMail(List<Mail> Mail) {
        mMail = Mail;
    }

    //Hechos para el siguiente proyecto donde se realizara el input de la agenda por lo que se
    // tendran que añadir/borrar contactos

    public void deleteItem(int position) {
        if (mMail != null & Objects.requireNonNull(mMail).size() > 0) {
            mMail.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }



    //Implements de la RecyclerView.Adapter<ViewHolder> necesarios al extenderla

    // onCreateViewHolder --> funcion que devuelve una vista del elemento personalizado ViewHolder
    @NonNull
    @Override
    public  com.example.adminappcall.ViewHolderFicha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_content, parent, false);
        return new ViewHolderFicha(view);
    }


    // onBindViewHolder --> funcion pasado un holder personalizado y una posicion de la lista de
    // contactos llama al onBind del holder para incluir la informacion y le crea el intent
    // de llamada al numero respectivo a la posicion de la lista de contactos
    @Override
    public void onBindViewHolder(@NonNull com.example.adminappcall.ViewHolderFicha holder, int position) {


        Mail mUsuario= mMail.get(position);
        holder.nombre.setText(mUsuario.getMail());
    }

    @Override
    public int getItemCount() {
        assert mMail != null;
        return Math.max(mMail.size(), 0);
    }
}
