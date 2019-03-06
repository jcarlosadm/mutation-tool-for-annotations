import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.*;

import br.com.caelum.tarefas.dao.TarefaDao;
import br.com.caelum.tarefas.modelo.Tarefa;

@stereotype.Controller
public class TarefasController {

    @org.springframework.beans.factory.annotation.Qualifier("jpaTarefaDao")
    @Autowired
    private TarefaDao dao;

    @Autowired
    private TarefaDao dao2;

    @Qualifier("jpaTarefaDao")
    private TarefaDao dao3;

    private String name;

    @RequestMapping(value = "novaTarefa", method = RequestMethod.POST)
    public String form() {
        return "tarefa/formulario";
    }

    @RequestMapping("adicionaTarefa")
    public String adiciona(@Valid("33") Tarefa tarefa, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("descricao")) {
            return "tarefa/formulario";
        }

        this.dao.adiciona(tarefa);
        return "tarefa/adicionada";
    }

    @RequestMapping("listaTarefas")
    public String lista(Model model) {
        model.addAttribute("tarefas", this.dao.lista());
        return "tarefa/lista";
    }

    @RequestMapping("removeTarefa")
    public String remove(Tarefa tarefa) {
        this.dao.remove(tarefa);
        return "redirect:listaTarefas";
    }

    @RequestMapping("mostraTarefa")
    public String mostra(Long id, Model model) {
        model.addAttribute("tarefa", this.dao.buscaPorId(id));
        return "tarefa/mostra";
    }

    @RequestMapping("alteraTarefa")
    private String altera(Tarefa tarefa) {
        System.out.println("tarefa == null: " + (tarefa == null));
        System.out.println("dao == null: " + (dao == null));
        this.dao.altera(tarefa);
        return "redirect:listaTarefas";
    }

    @RequestMapping("finalizaTarefa")
    private String finaliza(Long id, Model model) {
        this.dao.finaliza(id);
        model.addAttribute("tarefa", this.dao.buscaPorId(id));
        return "tarefa/finalizada";
    }
}