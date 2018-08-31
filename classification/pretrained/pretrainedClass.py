# Code referenced from https://github.com/spmallick/learnopencv/blob/master/Keras-ImageNet-Models/pretrained-imagenet-models-classification.ipynb

import keras
import numpy as np
from keras.applications import vgg16, inception_v3, resnet50, mobilenet

#THE REQUIRED PREPROCESSING FUNCTIONS
from keras.preprocessing.image import load_img
from keras.preprocessing.image import img_to_array
from keras.applications.imagenet_utils import decode_predictions
import matplotlib.pyplot as plt

import cv2
import argparse

#Load the VGG model
vgg_model = vgg16.VGG16(weights='imagenet')

#THESE ARE LARGE FILES THAT WE WONT RUN BECAUSE THEY AUTOMATICALLY DOWNLOAD
inception_model = inception_v3.InceptionV3(weights='imagenet')

resnet_model = resnet50.ResNet50(weights='imagenet')

mobilenet_model = mobilenet.MobileNet(weights='imagenet')

parser = argparse.ArgumentParser()
parser.add_argument("--image", help="relative path of image to be processed")
args = parser.parse_args()

if args.image:
    filename = args.image
#filename = '../../../../Desktop/test.jpg'

# load an image in PIL format
original = load_img(filename, target_size=(224, 224))
#print('PIL image size',original.size)
plt.imshow(original)
#plt.show()

# convert the PIL image to a numpy array
# IN PIL - image is in (width, height, channel)
# In Numpy - image is in (height, width, channel)
numpy_image = img_to_array(original)
plt.imshow(np.uint8(numpy_image))
#plt.show()
# print('numpy array size',numpy_image.shape)

# Convert the image / images into batch format
# expand_dims will add an extra dimension to the data at a particular axis
# We want the input matrix to the network to be of the form (batchsize, height, width, channels)
# Thus we add the extra dimension to the axis 0.
image_batch = np.expand_dims(numpy_image, axis=0)
# print('image batch size', image_batch.shape)
plt.imshow(np.uint8(image_batch[0]))
#plt.show()

#VGG16
# prepare the image for the VGG model# prepare 
processed_image = vgg16.preprocess_input(image_batch.copy())

# get the predicted probabilities for each class
predictions = vgg_model.predict(processed_image)
# print predictions
# convert the probabilities to class labels
# We will get top 5 predictions which is the default
label_vgg = decode_predictions(predictions)
# print(label_vgg)


#RESNET50
# prepare the image for the ResNet50 model
processed_image = resnet50.preprocess_input(image_batch.copy())

# get the predicted probabilities for each class
predictions = resnet_model.predict(processed_image)

# convert the probabilities to class labels
# If you want to see the top 3 predictions, specify it using the top argument
label_resnet = decode_predictions(predictions, top=3)
# print(label_resnet)

#MOBILENET

# prepare the image for the MobileNet model# prepare 
processed_image = mobilenet.preprocess_input(image_batch.copy())

# get the predicted probabilities for each class
predictions = mobilenet_model.predict(processed_image)

# convert the probabilities to imagenet class labels
label_mobilenet = decode_predictions(predictions)
# print(label_mobilenet)

#INCEPTIONV3

# load an image in PIL format# load an 
original = load_img(filename, target_size=(299, 299))

# Convert the PIL image into numpy array
numpy_image = img_to_array(original)

# reshape data in terms of batchsize
image_batch = np.expand_dims(numpy_image, axis=0)

# prepare the image for the Inception model
processed_image = inception_v3.preprocess_input(image_batch.copy())

# get the predicted probabilities for each class
predictions = inception_model.predict(processed_image)

# convert the probabilities to class labels
label_inception = decode_predictions(predictions)
# print(label_inception)

#Code to print labels on top of image
numpy_image = np.uint8(img_to_array(original)).copy()
numpy_image = cv2.resize(numpy_image,(900,900))


cv2.putText(numpy_image, "VGG16: {}, {:.2f}".format(label_vgg[0][0][1], label_vgg[0][0][2]) , (350, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 3)
cv2.putText(numpy_image, "MobileNet: {}, {:.2f}".format(label_mobilenet[0][0][1], label_mobilenet[0][0][2]) , (350, 75), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 3)
cv2.putText(numpy_image, "Inception: {}, {:.2f}".format(label_inception[0][0][1], label_inception[0][0][2]) , (350, 110), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 3)
cv2.putText(numpy_image, "ResNet50: {}, {:.2f}".format(label_resnet[0][0][1], label_resnet[0][0][2]) , (350, 145), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 3)

numpy_image = cv2.resize(numpy_image, (700,700))
# cv2.imwrite("images/{}_output.jpg".format(filename.split('/')[-1].split('.')[0]),cv2.cvtColor(numpy_image, cv2.COLOR_RGB2BGR))
cv2.imwrite('output.jpg',cv2.cvtColor(numpy_image, cv2.COLOR_RGB2BGR))
plt.figure(figsize=[10,10])
plt.imshow(numpy_image)
plt.axis('off')
#plt.show()
plt.savefig('foo.jpg')


