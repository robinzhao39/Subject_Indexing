# Subject_Indexing

An automatic subject indexing system is implemented in Java. Training data involves 24 documents in three topics, contained in C1,C4, C7. Documents first go through preprocessing steps of stop word removal, tokenization, lemmatization, and N-gram identification. Then, the result is vectorized by TF-IDF method. Self-implemented K-means++ algorithm clusters similar documents together, determined by either Cosine similarity or Euclidean distance. A visualization of the clustering result is generated after vectors are reduced into 2 dimensions by Principal Component Analysis. Key words of each document are also extracted based on TF-IDF frequencies.

After clustering, self-implemented KNN classification is used to predict the unknown topics. The resulting F1-score can achieve an average of 90% in 20 runs of the program. 
