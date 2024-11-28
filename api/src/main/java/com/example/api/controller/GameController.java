package com.example.api.controller;

import com.example.api.utils.ApiResponse;
import com.example.api.model.Game;
import com.example.api.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    //* Listar jogos
    @GetMapping
    public ResponseEntity<ApiResponse> getAllGames() {
        var games = gameRepository.findAll();

        if (games.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse(204, "Nenhum jogo cadastrado.", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Jogos listados com sucesso.", games));
    }

    //* Listar jogo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameRepository.findById(id);

        return game.map(g -> ResponseEntity.ok(new ApiResponse(200, "Jogo encontrado.", g)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(404, "Jogo não encontrado!", null)));
    }

    //* Criar jogo
    @PostMapping
    public ResponseEntity<ApiResponse> createGame(@Valid @RequestBody Game game) {
        game.calculateCurrentPrice();

        if (gameRepository.existsByName(game.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(409, "Jogo com este nome já existe!", null));
        }

        Game savedGame = gameRepository.save(game);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Jogo criado com sucesso!", savedGame));
    }

    //* Editar jogo por ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateGame(@PathVariable Long id, @Valid @RequestBody Game updatedGame) {
        return gameRepository.findById(id)
                .map(existingGame -> {
                    updatedGame.setId(existingGame.getId());
                    updatedGame.calculateCurrentPrice();
                    Game savedGame = gameRepository.save(updatedGame);
                    return ResponseEntity.ok(new ApiResponse(200, "Jogo atualizado com sucesso!", savedGame));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(404, "Jogo não encontrado!", null)));
    }

    //* Deletar jogo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGame(@PathVariable Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(204, "Sem conteúdo, exclusão realizada.", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, "Jogo não encontrado!", null));
    }
}
