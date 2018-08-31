# modified from source = https://github.com/fizyr/keras-retinanet/blob/master/examples/ResNet50RetinaNet.ipynb

import keras
import argparse

# import keras_retinanet
from keras_retinanet import models
from keras_retinanet.utils.image import read_image_bgr, preprocess_image, resize_image
from keras_retinanet.utils.visualization import draw_box, draw_caption
from keras_retinanet.utils.colors import label_color

# import miscellaneous modules
import matplotlib.pyplot as plt
import cv2
import os
import numpy as np
import time


# set tf backend to allow memory to grow, instead of claiming everything
import tensorflow as tf

# print("I'm in the python script")

def get_session():
    config = tf.ConfigProto()
    config.gpu_options.allow_growth = True
    return tf.Session(config=config)

    # set the modified tf session as backend in keras
keras.backend.tensorflow_backend.set_session(get_session())

# adjust this to point to your downloaded/trained model
# models can be downloaded here: https://github.com/fizyr/keras-retinanet/releases
model_path = os.path.join('resnet50_pascal_50.h5')

# load retinanet model
#model = models.load_model(model_path, backbone_name='resnet50')

# if the model is not converted to an inference model, use the line below
# see: https://github.com/fizyr/keras-retinanet#converting-a-training-model-to-inference-model
model = models.load_model(model_path, backbone_name='resnet50', convert=True)

# print(model.summary())

#load label to names mapping for visualization purposes
labels_to_names ={0:"blemish"}

#load image
parser = argparse.ArgumentParser()
parser.add_argument("--image", help="relative path of image to be processed")
args = parser.parse_args()

if args.image:
    filename = args.image
#filename = '../../../../Desktop/test.jpg'
image = read_image_bgr(filename)

# copy to draw on
draw = image.copy()
draw = cv2.cvtColor(draw, cv2.COLOR_BGR2RGB)

# preprocess image for network
image = preprocess_image(image)
image, scale = resize_image(image)

# process image
start = time.time()
boxes, scores, labels = model.predict_on_batch(np.expand_dims(image, axis=0))
# print("processing time: ", time.time() - start)

# correct for image scale
boxes /= scale

# visualize detections
for box, score, label in zip(boxes[0], scores[0], labels[0]):
    # scores are sorted so we can break
    if score < 0.5:
        break
        
    color = label_color(label)
    
    b = box.astype(int)
    draw_box(draw, b, color=color)
    
    caption = "{} {:.3f}".format(labels_to_names[label], score)
    draw_caption(draw, b, caption)
    
plt.figure(figsize=(15, 15)) 
plt.axis('off')
plt.imshow(draw)
plt.savefig("foo.jpg")
numpy_image = draw
numpy_image = cv2.resize(numpy_image,(900,900))
cv2.imwrite('output.jpg',cv2.cvtColor(numpy_image, cv2.COLOR_RGB2BGR))
#plt.show()