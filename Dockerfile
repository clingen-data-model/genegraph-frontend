FROM nginx
LABEL maintainer="Tristan Nelson <thnelson@geisinger.edu>"
COPY public /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80