package com.example.kevca.sistemaderecomendacion;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kevca.sistemaderecomendacion.bl.CarritoBL;
import com.example.kevca.sistemaderecomendacion.bl.ProductoBL;
import com.example.kevca.sistemaderecomendacion.bl.UsuarioBL;
import com.example.kevca.sistemaderecomendacion.domain.Carrito;
import com.example.kevca.sistemaderecomendacion.domain.Producto;
import com.example.kevca.sistemaderecomendacion.domain.Usuario;

import java.util.ArrayList;

import Adaptador.AdaptadorProducto;
import Adaptador.AdaptadorProductoCarrito;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorProductoCarrito adapter;
    private ArrayList<Producto> listaProductos;
    public static ProductoBL productoBL = ProductoBL.Companion.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("key");
            adapter = new AdaptadorProductoCarrito(this, value);
            //The key argument here must match that used in the other activity
        }else{
            adapter = new AdaptadorProductoCarrito(this, UsuarioBL.Companion.getSession());
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CarritoActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adapter.notifyDataSetChanged();
        Button clickButton = (Button) findViewById(R.id.btnPagar);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PagoActivity.class);
                startActivity(intent);
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
