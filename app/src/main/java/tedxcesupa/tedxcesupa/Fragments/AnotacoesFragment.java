package tedxcesupa.tedxcesupa.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import tedxcesupa.tedxcesupa.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnotacoesFragment extends Fragment {

    EditText anotacao;

    public AnotacoesFragment() {
        // Required empty public constructor
    }

    public static AnotacoesFragment newInstance(){
        AnotacoesFragment fragment = new AnotacoesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anotacoes, container, false);

        anotacao = view.findViewById(R.id.anotacaoEditText);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        anotacao.getText().toString();
        /* Salvar o conteudo do campo anotacao */


    }
}
