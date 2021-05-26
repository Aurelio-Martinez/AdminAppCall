package com.example.adminappcall;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubuserAdapter extends RecyclerView.Adapter<ViewHolderFicha> {

    // lista de clase Contacto
    private final List<Subusuario> msubusers;

    // Constructor con lista Contacto
    public SubuserAdapter(List<Subusuario> subuser) {
        msubusers = subuser;
    }



    //Implements de la RecyclerView.Adapter<ViewHolder> necesarios al extenderla

    // onCreateViewHolder --> funcion que devuelve una vista del elemento personalizado ViewHolder
    @NonNull
    @Override
    public  com.example.adminappcall.ViewHolderFicha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subuser_content, parent, false);
        return new ViewHolderFicha(view);
    }


    // onBindViewHolder --> funcion pasado un holder personalizado y una posicion de la lista de
    // contactos llama al onBind del holder para incluir la informacion y le crea el intent
    // de llamada al numero respectivo a la posicion de la lista de contactos
    @Override
    public void onBindViewHolder(@NonNull com.example.adminappcall.ViewHolderFicha holder, int position) {


        Subusuario mUsuario= msubusers.get(position);
        holder.nombre.setText(mUsuario.getNombre());
        String key = mUsuario.getKey();
        String nombre=mUsuario.getNombre();
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context,MainActivitySubuser.class);
            intent.putExtra("key",key);
            intent.putExtra("name",nombre);
            context.startActivity(intent); });
    }

    @Override
    public int getItemCount() {
        assert msubusers != null;
        return Math.max(msubusers.size(), 0);
    }
}
