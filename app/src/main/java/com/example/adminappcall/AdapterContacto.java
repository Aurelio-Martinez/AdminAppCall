 package com.example.adminappcall;


 import android.content.Context;
 import android.content.Intent;
 import android.content.res.ColorStateList;
 import android.graphics.Color;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import androidx.annotation.NonNull;
 import androidx.cardview.widget.CardView;
 import androidx.recyclerview.widget.RecyclerView;

 import java.util.List;


 public class AdapterContacto extends RecyclerView.Adapter<ViewHolderFicha> {

     // lista de clase Contacto
     private final List<Contacto> mcontactos;

     // Constructor con lista Contacto
     public AdapterContacto(List<Contacto> contactos) {
         mcontactos = contactos;
     }


     //Hechos para el siguiente proyecto donde se realizara el input de la agenda por lo que se
     // tendran que añadir/borrar contactos

     public void deleteItem(int position) {
         assert mcontactos != null;
         if (mcontactos.size() > 0) {
             mcontactos.remove(position);
         }
         notifyItemRemoved(position);
         notifyDataSetChanged();
     }

     //Implements de la RecyclerView.Adapter<ViewHolder> necesarios al extenderla

     // onCreateViewHolder --> funcion que devuelve una vista del elemento personalizado ViewHolder
     @NonNull
     @Override
     public  com.example.adminappcall.ViewHolderFicha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_content, parent, false);
         return new ViewHolderFicha(view);
     }


     // onBindViewHolder --> funcion pasado un holder personalizado y una posicion de la lista de
     // contactos llama al onBind del holder para incluir la informacion y le crea el intent
     // de llamada al numero respectivo a la posicion de la lista de contactos
     @Override
     public void onBindViewHolder(@NonNull com.example.adminappcall.ViewHolderFicha holder, int position) {

         Contacto mContacto= mcontactos.get(position);
         holder.onBind(mContacto);
         String key = mContacto.getKey();
         String subuser = mContacto.getOwner();
         CardView a= holder.itemView.findViewById(R.id.cardView);
         a.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4f9a94")));


         holder.itemView.setOnClickListener(v -> {
             Context context = v.getContext();
             Intent intent = new Intent(context, EditContacto.class);
             intent.putExtra("key",key);
             intent.putExtra("subuser",subuser);
             context.startActivity(intent);});
     }

     @Override
     public int getItemCount() {
         assert mcontactos != null;
         return Math.max(mcontactos.size(), 0);
     }
  }
