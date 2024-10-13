package jdev.mentoria.lojaVirtual.Loja_virtual;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.ApiTokenIntegracao;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.EmpresaTransporteDTO;
import okhttp3.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TesteAPIMelhorEnvio {

    public static void main(String[] args) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"orders\": [\n        \"9d28d681-955d-4c2f-81e4-010aecb9a73c\"\n    ]\n}");
        Request request = new Request.Builder()
                .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX+ "api/v2/me/shipment/tracking")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " +  ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
                .addHeader("User-Agent", "suporte@jdevtreinamento.com.br")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());




    }

}
