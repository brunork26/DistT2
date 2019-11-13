# DistT2

# Estrutura (Produtor ou Consumidor através dos argumentos)

- Semáforos 3
  - 1 para prioridade dos consumidores (Semáforo)
  - 1 para produtores nunca produzirem no mesmo lugar
  - 1 para consumidores nunca consumirem no mesmo lugar
  
- 1 Nodo que nunca morre (Chamar no terminal ao invés de outro PC), conterá: 
    - O "Buffer" compartilhado (Contadores ou array fixo)
    - Informações do Coordenador
    - Arquivo de Config Atualizado Referente aos nodos que estão rodando ao mesmo tempo
    - Listas de espera de Produtores e Consumidores atualizada em segundo plano 
    - Estado do sistema 
    - Bool Está em eleicao?
    
- Produtores:
    - 2 Nodos 
    - Produtores nao podem produzir enquanto houverem dados no buffer/contador
    - produtores nao podem produzir no mesmo lugar

- Consumidores:
    - 2 nodos
    - Consumidores tem prioridade
    - Consumidores nao podem possuir de uma mesma posição no buffer (semaforo) 
    
- Coordenador (Ele só coordena ou consume/produz tbm?):
    - Uma "interface" que coordena os outros nodos
    - Informações dos nodos
    - Guarda o buffer
    - listas de espera produtores/consumidores 
