# atividade-manus-ai que o professor passou em sala de aula.

# 💪 FitMeet — Treino Personalizado em Tempo Real

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jitsi_Meet-1D76BA?style=for-the-badge&logo=jitsi&logoColor=white" />
</p>

## 🎯 O Problema que o FitMeet Resolve

Com o aumento dos treinos online e personal trainers atendendo remotamente, a experiência costuma ser fragmentada. O aluno precisa usar o WhatsApp ou Zoom para o vídeo, o relógio do celular para o cronômetro, e um papel ou bloco de notas para contar as repetições. 

O **FitMeet** unifica tudo isso em uma única tela. Ele conecta o professor e o aluno em vídeo de alta qualidade e disponibiliza, na mesma interface, as ferramentas essenciais de academia: cronômetro digital e contador de repetições com registro de séries.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **SDK:** Android SDK 34
- **Videoconferência:** Jitsi Meet SDK (v9.2.2) via servidor público `meet.jit.si`
- **UI:** ViewBinding e XML com Material Design Components
- **Lógica:** Handler/Runnable para o Cronômetro

---

## 📱 Funcionalidades

1. **Lobby de Entrada:** Escolha seu nome, o código da sala e seu perfil (Aluno ou Personal).
2. **Videochamada Integrada:** Metade superior da tela exibe a câmera do professor e do aluno em tempo real.
3. **Cronômetro Digital:** Controle exato de tempo de execução e descanso com botões Iniciar, Pausar e Zerar.
4. **Contador de Repetições:** Botões grandes de `+` e `-` para não perder a conta durante a fadiga muscular, além de um botão `+ Série` para registrar o histórico do exercício.
5. **Notas do Treino:** Um campo rápido para o professor ou aluno fazer anotações em tempo real.

---

## 🌐 Pré-visualização Web Interativa

Antes de instalar o aplicativo, você pode testar a interface e a lógica do cronômetro e do contador através do nosso simulador web.

🔗 **Link de Pré-visualização:** [Acessar Simulador FitMeet](https://8080-iif9w42puv5n5e6xw44ko-f4b15920.us1.manus.computer)

> *Nota: O simulador web demonstra a UI e a lógica das ferramentas. Para testar a videoconferência real, compile o aplicativo Android.*

### 📸 Teste pelo Celular
Escaneie o QR Code abaixo para abrir o simulador direto no seu smartphone:

*(O QR Code está disponível no arquivo `qrcode_preview.png` enviado junto com este projeto).*

---

## 🛠️ Passo a Passo de Instalação (Como Rodar no Android Studio)

Siga estas instruções para compilar e rodar o projeto real no seu celular ou emulador.

### 1. Requisitos
- Android Studio Iguana (ou mais recente).
- Celular Android físico (recomendado para testar câmera) ou Emulador.
- Conexão com a internet.

### 2. Clonando e Abrindo o Projeto
1. Baixe a pasta `FitMeet` que foi entregue e extraia em seu computador.
2. Abra o Android Studio.
3. Clique em **File > Open** e selecione a pasta `FitMeet`.
4. Aguarde o Android Studio sincronizar o Gradle (ele fará o download do Jitsi Meet SDK, o que pode levar alguns minutos).

### 3. Rodando o App
1. Conecte seu celular Android via cabo USB (com a Depuração USB ativada) ou inicie um Emulador.
2. Na barra superior do Android Studio, clique no botão verde de **Play** (Run 'app').
3. O aplicativo será instalado no celular.
4. Ao abrir, permita o acesso à Câmera e ao Microfone.
5. Digite um código de sala (ex: `treino-teste`) e clique em **INICIAR TREINO**.

---

## 📦 Como Gerar o APK para o Professor

Se você precisa apenas enviar o arquivo `.apk` para o seu professor instalar no celular dele, siga estes passos dentro do Android Studio:

1. Com o projeto aberto, vá no menu superior e clique em **Build**.
2. Selecione **Build Bundle(s) / APK(s)**.
3. Clique em **Build APK(s)**.
4. O Android Studio começará a compilar o projeto (uma barra de progresso aparecerá embaixo).
5. Quando terminar, uma notificação aparecerá no canto inferior direito dizendo *"APK(s) generated successfully"*.
6. Clique no link azul **"locate"** nessa notificação.
7. Uma pasta abrirá no seu computador mostrando o arquivo `app-debug.apk`.
8. Renomeie esse arquivo para `FitMeet.apk` e envie para o seu professor (via WhatsApp, E-mail ou Google Drive).

> **Aviso de Segurança para o Professor:** Como o APK não está sendo baixado da Google Play Store, o celular do professor pode exibir um aviso de "App de origem desconhecida". Ele precisará clicar em "Configurações" e permitir a instalação desta fonte.

---
*Desenvolvido com dedicação por um Desenvolvedor Android Sênior.*
