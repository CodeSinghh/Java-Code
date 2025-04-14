import os
import math
import string
from collections import defaultdict, Counter

class SearchEngine:
    def __init__(self, directory):
        self.directory = directory
        self.inverted_index = defaultdict(set)
        self.documents = {}
        self.document_frequencies = defaultdict(int)
        self._index_files()

    def _tokenize(self, text):
        text = text.lower().translate(str.maketrans('', '', string.punctuation))
        return text.split()

    def _index_files(self):
        for filename in os.listdir(self.directory):
            filepath = os.path.join(self.directory, filename)
            if os.path.isfile(filepath) and filepath.endswith('.txt'):
                with open(filepath, 'r', encoding='utf-8') as f:
                    text = f.read()
                    tokens = self._tokenize(text)
                    self.documents[filename] = tokens
                    counted = set()
                    for word in tokens:
                        self.inverted_index[word].add(filename)
                        if word not in counted:
                            self.document_frequencies[word] += 1
                            counted.add(word)

    def _tf_idf(self, word, doc_tokens):
        tf = doc_tokens.count(word) / len(doc_tokens)
        idf = math.log(len(self.documents) / (1 + self.document_frequencies[word]))
        return tf * idf

    def search(self, query):
        query_tokens = self._tokenize(query)
        result_scores = defaultdict(float)

        for word in query_tokens:
            for doc in self.inverted_index.get(word, []):
                result_scores[doc] += self._tf_idf(word, self.documents[doc])

        return sorted(result_scores.items(), key=lambda x: x[1], reverse=True)

if __name__ == "__main__":
    engine = SearchEngine("documents")  # Folder containing .txt files
    while True:
        query = input("\nEnter your search query (or type 'exit'): ")
        if query.lower() == 'exit':
            break
        results = engine.search(query)
        if not results:
            print("No results found.")
        else:
            print("Top results:")
            for doc, score in results[:5]:
                print(f"{doc} (Score: {score:.4f})")
