package com.example.cadastroclientes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ActCadEndereco extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtCPF;
    FloatingActionButton fab2;
    private String HOST="http://192.168.0.103/jbeapp/";
    private String nome, email,telefone,cpf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_endereco);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab2 = findViewById(R.id.fab);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtCPF=(EditText) findViewById(R.id.edtTelefone);
    }
    public boolean validaCampos(){

        nome = this.edtNome.getText().toString();
        email = this.edtEmail.getText().toString();
        telefone = this.edtTelefone.getText().toString();
        cpf=this.edtCPF.getText().toString();

        boolean res = false;
        if(res = isCampoVazio(nome)) edtNome.requestFocus();

        else if (res = !isEmailValido(email)) edtEmail.requestFocus();

        else if (res = isCampoVazio(telefone)) edtTelefone.requestFocus();

        if(res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.titles_aviso);
            dlg.setMessage(R.string.message_campos_invalidos_brancos);
            dlg.setNeutralButton(R.string.lblOK,null);
            dlg.show();
            return false;
        }
        return true;
    }

    public boolean isEmailValido(String email){
        boolean resultado = ((!isCampoVazio(email)) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor)|| valor.trim().isEmpty());
        return resultado;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_cliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_ok:
                validaCampos();
                Toast.makeText(this, "Botão OK selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_cancelar:
                Toast.makeText(this, "Botão Cancelar selecionado", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cadastrar(View view){
        if(!validaCampos())
            return;
        String url = HOST + "create.php";
        //Create
        //POST e GET
        Ion.with(ActCadEndereco.this)
                .load(url)
                .setBodyParameter("name", nome)
                .setBodyParameter("email", email)
                .setBodyParameter("phone", telefone)
                .setBodyParameter("cpf", cpf)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //System.out.println(e.getMessage().toString());
                        // do stuff with the result or error
                        if(result!=null){
                            int idCadastro =Integer.parseInt((result.get("ID").getAsString()));

                            if(result.get("CREATE").getAsString().equals("OK")){
                                Toast.makeText(ActCadEndereco.this, "Salvo com Sucesso", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ActCadEndereco.this, "Ocorreu um erro ", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(ActCadEndereco.this, "Ocorreu um erro ao Salvar", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //Intent it = new Intent(ActCadCliente.this, ActCadEndereco.class);
        //startActivity(it);
    }
}
