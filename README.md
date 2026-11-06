# Enterprise AI Knowledge Assistant

An AI-powered document management and question-answering system built with Java 21, Spring Boot, Spring AI, and a microservices architecture.

The application allows users to upload PDF documents, extract their content, generate vector embeddings, and ask natural-language questions using **Retrieval-Augmented Generation (RAG)**.

## Technologies Used

- **Backend:** Java 21, Spring Boot, Spring Web, Spring Data JPA.
- **Artificial Intelligence:** Spring AI, Ollama, Qwen3 4B, and nomic-embed-text.
- **Databases:** PostgreSQL 18 and pgvector.
- **Architecture:** Microservices and API Gateway.
- **Containerization:** Docker and Docker Compose.
- **Orchestration:** Kubernetes.
- **Testing:** JUnit 5, Mockito, and Maven.
- **Document Processing:** PDF text extraction and processing.

## Architecture

The application consists of four independent microservices:

| Microservice | Port | Responsibility |
|---|---|---|
| user-service | 8081 | User management |
| document-service | 8082 | PDF upload, storage, and text extraction |
| ai-service | 8083 | Embedding generation, vector indexing, and RAG queries |
| api-gateway | 8080 | Central entry point and request routing |

Each microservice has its own Maven project and configuration.

### RAG Workflow

1. A user uploads a PDF document through the API Gateway.
2. `document-service` validates the file, extracts its text, and stores the document information.
3. `ai-service` retrieves the extracted text and splits it into smaller chunks.
4. Ollama generates vector embeddings using the `nomic-embed-text` model.
5. The embeddings are stored in PostgreSQL using the pgvector extension.
6. The user submits a natural-language question about the document.
7. The system retrieves relevant document chunks and uses `qwen3:4b` to generate a contextual answer.

## Main Features

- User management.
- PDF document upload and retrieval.
- PDF validation with a maximum file size of 10 MB.
- Automatic text extraction from PDF documents.
- Vector embedding generation and storage.
- Semantic search using pgvector.
- Document-based question answering using RAG.
- Local, Docker Compose, and Kubernetes deployment.
- Automated testing with JUnit 5 and Mockito.

## Running the Application

### Prerequisites

- Java 21
- Maven
- Docker Desktop
- Kubernetes with kubectl
- PostgreSQL 18 with pgvector
- Ollama with the following models:
  - `qwen3:4b`
  - `nomic-embed-text`

### 1. Local Development

Start the required PostgreSQL databases and Ollama.

Run each microservice from its corresponding directory:

```bash
cd user-service
mvn spring-boot:run
```

```bash
cd document-service
mvn spring-boot:run
```

```bash
cd ai-service
mvn spring-boot:run
```

```bash
cd api-gateway
mvn spring-boot:run
```

Configure the required database connection properties and service URLs before starting the application.

The API Gateway is available at:

`http://localhost:8080`

### 2. Docker Compose

From the project root directory, build the required Docker images and start the containers:

```bash
docker compose up -d
```

Check the running containers:

```bash
docker compose ps
```

View the logs:

```bash
docker compose logs -f
```

Stop the application:

```bash
docker compose down
```

In the current Docker Compose configuration, the API Gateway is accessible at:

`http://localhost:8085`

Ollama must be running and reachable from the AI service.

### 3. Kubernetes

Make sure Docker Desktop Kubernetes is enabled and the cluster is running.

Deploy the PostgreSQL databases:

```bash
kubectl apply -f k8s/user-db.yaml
kubectl apply -f k8s/document-db.yaml
kubectl apply -f k8s/vector-db.yaml
```

Deploy the microservices:

```bash
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/document-service.yaml
kubectl apply -f k8s/ai-service.yaml
kubectl apply -f k8s/api-gateway.yaml
```

Verify the deployment:

```bash
kubectl get deployments
kubectl get pods
kubectl get services
```

Enable the pgvector extension in the vector database if it has not already been initialized:

```bash
kubectl exec deployment/enterprise-ai-vector-db -- psql -U postgres -d enterprise_ai_vectors -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

Access the API Gateway using port forwarding:

```bash
kubectl port-forward service/enterprise-ai-api-gateway 8090:8080
```

The API Gateway is then accessible at:

`http://localhost:8090`

Keep the terminal running while using port forwarding. Closing it stops the local connection but does not stop the Kubernetes services.

**Note:** The Kubernetes manifests use local Docker images and development database credentials. Production deployment requires additional security and configuration measures.

## API Endpoints

All endpoints are accessible through the API Gateway.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/ai/test` | Check AI service availability |
| POST | `/documents` | Upload a PDF document |
| GET | `/documents` | List uploaded documents |
| POST | `/ai/index-document/{documentId}` | Generate and store document embeddings |
| POST | `/ai/ask` | Submit a general AI question |
| POST | `/ai/ask-document` | Ask a question about an indexed document |

### API Usage Examples

The following examples use the Kubernetes API Gateway, accessible at `http://localhost:8090` through port forwarding.

#### 1. Upload a PDF Document

```powershell
curl.exe -X POST "http://localhost:8090/documents" -F "file=@C:\path\to\document.pdf"
```

Example response:

```json
{
  "id": 1,
  "filename": "document.pdf",
  "contentType": "application/pdf",
  "size": 117164,
  "uploadedAt": "2026-11-06T10:58:34.733364051"
}
```

#### 2. Index a Document

Generate embeddings and store them in pgvector:

```powershell
curl.exe -X POST "http://localhost:8090/ai/index-document/1"
```

Expected response:

```text
Document indexed successfully: 1
```

#### 3. Ask a Question About a Document

Using Windows PowerShell:

```powershell
$body = @{
    documentId = 1
    question = "What is this document about?"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8090/ai/ask-document" `
    -Method POST `
    -ContentType "application/json; charset=utf-8" `
    -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
```

The API returns an AI-generated answer based on relevant document fragments retrieved from pgvector.

#### 4. Verify Indexed Document Chunks

Check the number of stored chunks per document:

```powershell
kubectl exec deployment/enterprise-ai-vector-db -- psql -U postgres -d enterprise_ai_vectors -c "SELECT metadata->>'documentId' AS document_id, COUNT(*) AS chunks FROM vector_store GROUP BY metadata->>'documentId';"
```

## Running Tests

Each microservice has its own Maven project.

Run the automated tests separately:

```powershell
cd user-service
mvn test

cd ../document-service
mvn test

cd ../ai-service
$env:VECTOR_DB_URL = "jdbc:postgresql://localhost:5434/enterprise_ai_vectors"
mvn test

cd ../api-gateway
mvn test
```

The AI service integration tests require an accessible PostgreSQL database with pgvector.

## Project Status

The application has been developed and tested in three environments:

- Local development with Spring Boot.
- Containerized deployment with Docker Compose.
- Kubernetes deployment with seven application and database components.

The complete RAG workflow has been verified in Kubernetes, including PDF upload, text extraction, embedding generation, vector storage, semantic retrieval, and AI-generated answers.