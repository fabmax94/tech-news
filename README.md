# Tech News

Aplicativo Android para agregar notícias de tecnologia de múltiplas fontes RSS em um único feed. Desenvolvido para profissionais de tecnologia que querem se manter atualizados sem depender de plataformas pagas.

## Funcionalidades

- Feed unificado de 15 fontes de tecnologia
- Filtro por categoria (Geral, Desenvolvimento, IA, Segurança, Mobile, Cloud, Open Source, Startups)
- Busca em tempo real nos artigos
- Salvar artigos para ler depois
- Ativar/desativar fontes individualmente
- Atualização automática em background a cada 2 horas
- Cache local de 7 dias
- Suporte a tema escuro e Dynamic Color (Android 12+)

## Fontes

| Fonte | Categoria |
|-------|-----------|
| Hacker News | Geral |
| The Verge | Geral |
| Ars Technica | Geral |
| Wired | Geral |
| TechCrunch | Startups |
| Dev.to | Desenvolvimento |
| InfoQ | Desenvolvimento |
| CSS-Tricks | Desenvolvimento |
| Kotlin Blog | Desenvolvimento |
| MIT Technology Review | Inteligência Artificial |
| Android Developers Blog | Mobile |
| SecurityWeek | Segurança |
| Krebs on Security | Segurança |
| AWS News Blog | Cloud |
| GitHub Blog | Open Source |

## Stack

- **UI:** Jetpack Compose + Material 3
- **Arquitetura:** Clean Architecture + MVVM
- **DI:** Hilt
- **Banco de dados:** Room
- **Rede:** Retrofit + OkHttp
- **Imagens:** Coil 3
- **Background:** WorkManager
- **Parser:** RSS 2.0 e Atom (implementação própria com XmlPullParser + Jsoup)

## Requisitos

- Android 8.0+ (API 26)
- Android Studio Ladybug ou superior
- JDK 17

## Build

Clone o repositório e crie o arquivo `local.properties` na raiz:

```
sdk.dir=/caminho/para/Android/sdk
```

```bash
# Build debug
./gradlew assembleDebug

# Instalar no dispositivo/emulador conectado
./gradlew installDebug
```

O APK debug é gerado em `app/build/outputs/apk/debug/app-debug.apk`.
