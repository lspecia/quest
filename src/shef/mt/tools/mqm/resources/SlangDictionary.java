package shef.mt.tools.mqm.resources;

import shef.mt.tools.Resource;
import shef.mt.tools.mqm.Configurable;
import shef.mt.tools.mqm.MQMManager;
import shef.mt.tools.mqm.Registrable;

/**
 * monolingual or bilingual slang dict
 * @author <a href="mailto:mail.jie.jiang@gmail.com">Jie Jiang</a>
 * @date 9/11/13
 */
public class SlangDictionary extends Resource implements Configurable, Registrable {



    public SlangDictionary(String language) {

    }

    @Override
    public boolean isConfigured(MQMManager mqmManager) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void load(MQMManager mqmManager) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void register(MQMManager mqmManager) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
