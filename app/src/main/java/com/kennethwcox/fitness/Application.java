package com.kennethwcox.fitness;

public class Application extends android.app.Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		ReportHandler.install(this, "kenneth.cox5.kc@gmail.com");
	}
}

