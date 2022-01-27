# JFace
Proyecto de reconocimiento facial como parte del protocolo de investigación para la materia 'Taller de Investigación II'.


## Descripción 
La primera etapa para el reconocimiento de una persona es la detección facial, proceso que se realiza a través de un conjunto de imágenes que se obtienen en tiempo real.

El algoritmo empleado para la detección el resto es el de Viola-Jones. Este incorpora la idea de imagen integral, que en conjunto con el método de boost para el entrenamiento, resulta en un clasificador robusto y preciso. Viola-Jones detecta en la imagen, las partes deseadas a partir de varios clasificadores disponibles. El sistema de detección de Viola-Jones utiliza grupos de características simples para llevar a cabo la detección. El uso de estas características posibilita una velocidad mucho mayor a la hora de detectar caras frente a un sistema basado en píxeles.

Para el reconocimiento facial, el algoritmo utlizado es el de Análisis de Componentes Principales (PCA) o también llamada Eigenfaces.


## OpenCV 
Para realizar la detección de rostros con esta OpenCV se usa el módulo cv: CascadeClassifier, el cual contiene una función para cargar las características de Haar del objeto a detectar, y otra para la detección propiamente dicha.


## Interfaz gráfica
![interfaz1](/img/img1.png)

![interfaz3](/img/img3.png)


## Diagramas
### Diagrama de casos de uso
![Diagrama de casos de uso](/img/img_casosuso.png)

### Diagramas de estados
![Diagrama de estados de reconocimiento](/img/img_edos2.png)
