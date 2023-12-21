package com.unika;

import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import com.unika.model.PessoaFisica;
import com.unika.model.TipoPessoa;
import de.agilecoders.wicket.core.Bootstrap;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.unika.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// Testes
		MonitoradorApi monitoradorApi = new MonitoradorApi();
        try {
			PessoaFisica pessoaFisica = new PessoaFisica();
			pessoaFisica.setNome("Osmar Neto");
			pessoaFisica.setCpf("06079931117");
			pessoaFisica.setRg("6295959");
			pessoaFisica.setEmail("osmarneto@gmail.com");
			pessoaFisica.setTipoPessoa(TipoPessoa.PESSOA_FISICA);
			pessoaFisica.setInscricaoEstadual("98569856985");
			pessoaFisica.setDataNascimento("2000-06-20");

			System.out.println(monitoradorApi.cadastrarMonitorador(pessoaFisica));

//			List<Monitorador> monitoradors = monitoradorApi.listarMonitoradores();
//			monitoradors.forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bootstrap.install(this);

		// add your configuration here
	}
}
