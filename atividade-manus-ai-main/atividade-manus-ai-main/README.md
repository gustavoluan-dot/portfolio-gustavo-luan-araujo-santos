# 💪 FitMeet | Solução de Treinamento Remoto em Tempo Real

<div align="center">
  <img src="./preview/preview.png.png" alt="Interface FitMeet" width="900">
</div>

## 📝 Descrição do Projeto

O **FitMeet** é uma aplicação de videoconferência interativa arquitetada para otimizar a dinâmica de treinamentos remotos. O sistema resolve o problema da fragmentação de ferramentas de terceiros ao unificar comunicação de vídeo em alta definição, cronometragem e monitoramento de performance em uma única interface centralizada.

## 🚀 Stack Tecnológico

A arquitetura da solução prioriza estabilidade de conexão e baixa latência de transmissão:

* **Linguagem Principal:** Kotlin
* **Framework:** Android SDK (API Level 34)
* **Comunicação de Vídeo:** Jitsi Meet SDK 9.2.2 (Implementação WebRTC)
* **Infraestrutura de Sinalização:** `meet.jit.si`
* **Interface (UI):** Material Design 3 e ViewBinding
* **Lógica de Concorrência:** Utilização de `Handler` e `Runnable` para o gerenciamento assíncrono das threads do cronômetro.

## 🏗️ Estrutura do Repositório

A organização do código-fonte segue o padrão estrutural de projetos nativos Android:


```text
atividade-manus-ai-main/
│
├── app/                ← Módulos de código-fonte e lógica de negócio
├── preview/            ← Assets de documentação visual
├── gradle/wrapper/     ← Gerenciamento de artefatos de build
├── build.gradle        ← Configurações globais de compilação
└── qrcode_preview.png  ← Endpoint de acesso rápido ao aplicativo
