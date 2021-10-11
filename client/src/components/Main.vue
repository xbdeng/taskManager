<template>
  <div class='mainFrame'>
    <el-container class='mainFrameContainer'>

      <!--Task Manager and hello username-->
      <el-header class="head">
        <el-row class="navigator">
          <el-col :span="4">
            <div class="title">Task Manager</div>
          </el-col>
          <el-col :span=4 :offset="16">
            <div class="hello">Hi,{{username}}</div>
          </el-col>
        </el-row>
      </el-header>

      <!--Choose button to find project information-->
      <el-header class='projectSelector'>
        <el-row :gutter='90' class='projects'>
          <el-col :span="2">
            <el-button>Project1</el-button>
          </el-col>
          <el-col :span="2">
            <el-button>Project2</el-button>
          </el-col>
          <el-col :span="2">
            <el-button>Project3</el-button>
          </el-col>
          <el-col :span="2">
            <el-button>Project4</el-button>
          </el-col>
        </el-row>
      </el-header>

      <!--Asider of the main frame-->
      <el-container>
        <el-aside class="aside">
          <el-menu>
          <el-menu-item>Overview</el-menu-item>
          <el-menu-item @click="toProfile">Personal Information</el-menu-item>
          <el-menu-item>Announcement</el-menu-item>
          <el-menu-item>Event Schedule</el-menu-item>
          <el-menu-item>Team Forming</el-menu-item>
          <el-menu-item>Arrangement</el-menu-item>
          <el-menu-item>Submission</el-menu-item>
          <el-menu-item>Gradebook</el-menu-item>
          <el-menu-item>Others</el-menu-item>
          </el-menu>
        </el-aside>

        <el-container>
          <el-main>
            <el-header>
              <el-menu mode='horizontal'>
                <el-menu-item>Team</el-menu-item>
                <el-menu-item>Students</el-menu-item>
              </el-menu>
            </el-header>

            <el-table border :data='tableData'>
              <el-table-column label="ID" prop='id'></el-table-column>
              <el-table-column label="Topic" prop='topic'></el-table-column>
              <el-table-column label="Numbers" prop='numbers'></el-table-column>
              <el-table-column label="Tags" prop='tags'></el-table-column>
            </el-table>

          </el-main>
          <el-footer>
            <el-row>
              <el-col :span='20'>
                <el-input placeholder="Please input something"></el-input>
              </el-col>
              <el-col :span='4'>
                <el-button type='primary'>create</el-button>
              </el-col>
            </el-row>
            
          </el-footer>
        </el-container>

      </el-container>
      

      
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Main",
  props: ['username'],
  data() {
    return {
      tableData: [
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        },
        {
          id:'1',
          topic:'project1',
          numbers:'2/5',
          tags:'tag0,tag1'
        }
      ]
    }
  },
  methods:{
    toProfile(event){
      this.$router.push({name: 'Profile',params:{username:this.username}});
    }
  }
}
</script>

<style scoped>
.mainFrame{
  width: 100%;
  height:100%;
  position:fixed;
}
.mainFrameContainer{
  width: 100%;
  height:100%;
  position:fixed;
}
.head{
  background: #0099ff;
}
.projectSelector{
  background: whitesmoke;
  box-shadow: 0 2px 4px rgba(0,0,0,.12),0 0 6px rgba(0,0,0,.04);
}
.projects{
  top:10px;
}
.navigator{
  top:10px;
}
.title{
  font-size: 30px;
  color:white;
}
.aside{
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 6px 0 6px rgba(0, 0, 0, .04);
}

.hello{
  font-size: 30px;
  color:white;
}

.pos{
  top:10px;
}
</style>
