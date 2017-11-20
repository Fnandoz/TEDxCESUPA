package tedxcesupa.tedxcesupa.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tedxcesupa.tedxcesupa.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformacoesFragment extends Fragment {


    public InformacoesFragment() {
        // Required empty public constructor
    }

    public static InformacoesFragment newInstance(){
        InformacoesFragment fragment = new InformacoesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informacoes, container, false);
    }

}
