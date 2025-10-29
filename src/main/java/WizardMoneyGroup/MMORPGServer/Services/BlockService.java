package WizardMoneyGroup.MMORPGServer.Services;

import WizardMoneyGroup.MMORPGServer.DAO.BlockRepository;
import WizardMoneyGroup.MMORPGServer.Models.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    private final BlockRepository blockRepository;

    @Autowired
    public BlockService(BlockRepository blockRepository){
        this.blockRepository = blockRepository;
    }

    public void addBlock(Block block){
        blockRepository.save(block);
        return;
    }
    public void removeBlock(Block block){
        blockRepository.delete(block);
        return;
    }
    public List<Block> findAllBlocks(){
        return blockRepository.findAll();
    }
}
