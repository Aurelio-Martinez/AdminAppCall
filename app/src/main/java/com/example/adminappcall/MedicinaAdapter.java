package com.example.adminappcall;



import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;


public class MedicinaAdapter extends RecyclerView.Adapter<ViewHolderFicha> {

    // lista de clase Contacto
    private final List<Medicina> mMedicinas;

    // Constructor con lista Contacto
    public MedicinaAdapter(List<Medicina> medicinas) {
        mMedicinas = medicinas;
    }


    //Hechos para el siguiente proyecto donde se realizara el input de la agenda por lo que se
    // tendran que añadir/borrar contactos

    public void deleteItem(int position) {
        assert mMedicinas != null;
        if (mMedicinas.size() > 0) {
            mMedicinas.remove(position);
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

        Medicina mMedicina= mMedicinas.get(position);
        holder.onBind(mMedicina);
        Calendar a,c;
        a = Calendar.getInstance();
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, Math.toIntExact(mMedicina.year));
        c.set(Calendar.DAY_OF_MONTH, Math.toIntExact(mMedicina.dia));
        c.set(Calendar.MONTH, Math.toIntExact(mMedicina.mes)-1);

        CardView b= holder.itemView.findViewById(R.id.cardView);

        if(mMedicina.getYear()>a.get(Calendar.YEAR) || (mMedicina.getYear()==a.get(Calendar.YEAR) && mMedicina.getMes() > (a.get(Calendar.MONTH)+1))){
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4f9a94")));
        }else{
            if((mMedicina.getYear()==a.get(Calendar.YEAR) && mMedicina.getMes() == (a.get(Calendar.MONTH)+1) && (mMedicina.getDia() )>=a.get(Calendar.DAY_OF_MONTH))){
                b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ccd461")));
            }else{
                b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9A4F55")));
            }
        }
    }

    @Override
    public int getItemCount() {
        assert mMedicinas != null;
        return Math.max(mMedicinas.size(), 0);
    }
}
