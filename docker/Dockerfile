FROM clojure:temurin-21-bookworm

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get clean 
RUN apt-get update 
RUN apt-get install -y bash sudo locales wget busybox 
# Environment customization.
RUN apt-get install -y tmux gcc unzip ripgrep
RUN apt-get autoclean

# Add clojure-LSP locally.
RUN wget https://github.com/clojure-lsp/clojure-lsp/releases/download/2024.03.01-11.37.51/clojure-lsp-native-static-linux-amd64.zip -O - | \
    busybox unzip - && \
    chmod 755 clojure-lsp && \
    mv clojure-lsp /usr/local/bin/

#
# Add any editors locally for development within the container.
#

# NeoVIM AppImage.
RUN wget https://github.com/neovim/neovim/releases/download/nightly/nvim.appimage && \
    chmod 755 nvim.appimage && \
    ./nvim.appimage --appimage-extract && \
    cp -a squashfs-root/usr /


WORKDIR /root
USER root

CMD ["sleep", "infinity"]
