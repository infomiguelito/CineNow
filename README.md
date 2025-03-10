# CineNow - Recomendador de Filmes com IA

CineNow é um aplicativo Android que utiliza Inteligência Artificial para recomendar filmes personalizados com base nas preferências e histórico do usuário.

## 🚀 Funcionalidades

- Sistema de recomendação de filmes usando TensorFlow Lite
- Recomendações baseadas em:
  - Histórico de filmes assistidos
  - Preferências de gêneros
  - Popularidade dos filmes
  - Avaliações do usuário

## 🛠️ Tecnologias Utilizadas

- Kotlin
- Jetpack Compose
- TensorFlow Lite
- Retrofit
- Room Database
- Coroutines
- Material Design 3

## 📋 Pré-requisitos

- Android Studio Hedgehog | 2023.1.1 ou superior
- JDK 8 ou superior
- Android SDK 24+
- Kotlin 1.9+

## 🔧 Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/CineNow.git
```

2. Abra o projeto no Android Studio

3. Configure sua API key do TMDB:
   - Crie um arquivo `local.properties` na raiz do projeto
   - Adicione sua chave: `API_KEY="sua_chave_aqui"`

4. Sincronize o projeto com o Gradle

5. Execute o aplicativo

## 📱 Como usar

1. Ao abrir o app pela primeira vez, você será solicitado a:
   - Selecionar seus gêneros favoritos
   - Avaliar alguns filmes para calibrar as recomendações

2. O sistema de IA processará suas preferências e histórico

3. Você receberá recomendações personalizadas de filmes

## 🤝 Contribuindo

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE.md](LICENSE.md) para detalhes

## ✨ Próximos Passos

- [ ] Implementar recomendações offline
- [ ] Adicionar suporte a múltiplos usuários
- [ ] Integrar com mais serviços de streaming
- [ ] Melhorar a precisão do modelo de IA
