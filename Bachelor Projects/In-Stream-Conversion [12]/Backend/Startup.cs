using Bsc_In_Stream_Conversion.Controllers;
using Bsc_In_Stream_Conversion.Database;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;
using Serilog;
using Serilog.Configuration;
using Serilog.Filters;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Bsc_In_Stream_Conversion.MQTT;

namespace Bsc_In_Stream_Conversion
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {

            services.AddControllers();
            services.AddSignalR();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "Bsc_In_Stream_Conversion", Version = "v1" });
            });
            services.AddSingleton<IStreamClientManager, MQTTClientManager>();
            services.AddSingleton<IDatabaseAccess, DatabaseAccess>();
            services.AddSingleton<UnitFactory>();
            
            services.AddScoped<SocketRequestHandler>();
            Log.Logger = new LoggerConfiguration()
                .WriteTo.File(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) +  $"/logs/log-.txt", rollingInterval: RollingInterval.Day)
                .CreateLogger();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, IHostApplicationLifetime lifetime)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Bsc_In_Stream_Conversion v1"));
            }

            app.UseHttpsRedirection();
            
            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
                endpoints.MapHub<SubscribeHub>("/SubscribeHub");
            });

            lifetime.ApplicationStopped.Register(OnShutdown);

            PerformanceMeasurer.StartTime = DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString();
        }

        private void OnShutdown()
        {
            Log.CloseAndFlush();
        }
    }
}
