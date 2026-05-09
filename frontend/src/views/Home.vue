<template>
  <div class="home">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">🎬 热门电影</h1>
        <p class="page-subtitle">发现好电影，分享真感受</p>
      </div>

      <!-- 搜索和筛选区 -->
      <div class="filter-section">
        <div class="search-box">
          <el-input
            v-model="keyword"
            placeholder="搜索电影名称、导演、演员..."
            prefix-icon="el-icon-search"
            clearable
            @clear="loadMovies"
            @keyup.enter.native="loadMovies"
          />
        </div>
        <div class="filter-tags">
          <el-select v-model="genre" placeholder="全部类型" clearable @change="loadMovies">
            <el-option v-for="g in genres" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select v-model="region" placeholder="全部地区" clearable @change="loadMovies">
            <el-option v-for="r in regions" :key="r" :label="r" :value="r" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="loadMovies">搜索</el-button>
        </div>
      </div>

      <!-- 电影列表 -->
      <div class="movie-list">
        <!-- 骨架屏加载状态 -->
        <div class="movie-grid" v-if="loading">
          <MovieCardSkeleton v-for="n in 10" :key="'skeleton-' + n" />
        </div>
        
        <!-- 空状态 -->
        <div v-else-if="movies.length === 0" class="empty-tip">
          <el-empty description="暂无电影数据">
            <el-button type="primary" @click="resetFilters">重置筛选</el-button>
          </el-empty>
        </div>
        
        <!-- 电影卡片列表 -->
        <div class="movie-grid" v-else>
          <div 
            v-for="movie in movies" 
            :key="movie.id" 
            class="movie-card"
            @click="goToDetail(movie.id)"
          >
            <div class="poster-wrapper">
              <LazyImage :src="movie.poster || defaultPoster" :alt="movie.title" />
              <div class="rating-badge" v-if="movie.avgRating > 0">
                <i class="el-icon-star-on"></i>
                <span class="score">{{ movie.avgRating }}</span>
              </div>
              <div class="card-overlay">
                <span class="view-detail">查看详情</span>
              </div>
            </div>
            <div class="movie-info">
              <h3 class="title">{{ movie.title }}</h3>
              <div class="meta">
                <span class="tag genre-tag">{{ movie.genre }}</span>
                <span class="tag region-tag">{{ movie.region }}</span>
              </div>
              <p class="director"><i class="el-icon-user"></i> {{ movie.director }}</p>
              <p class="rating-count"><i class="el-icon-chat-dot-round"></i> {{ movie.ratingCount }}人评价</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { movieApi } from '@/api'
import LazyImage from '@/components/LazyImage.vue'
import MovieCardSkeleton from '@/components/MovieCardSkeleton.vue'

export default {
  name: 'Home',
  components: {
    LazyImage,
    MovieCardSkeleton
  },
  data() {
    return {
      movies: [],
      genres: [],
      regions: [],
      keyword: '',
      genre: '',
      region: '',
      loading: false,
      defaultPoster: '/images/default-poster.jpg'
    }
  },
  created() {
    this.loadFilters()
    this.loadMovies()
  },
  methods: {
    async loadFilters() {
      try {
        const res = await movieApi.getFilters()
        this.genres = res.data.genres || []
        this.regions = res.data.regions || []
      } catch (e) {
        console.error('加载筛选项失败', e)
      }
    },
    async loadMovies() {
      this.loading = true
      try {
        const res = await movieApi.getList({
          genre: this.genre,
          region: this.region,
          keyword: this.keyword
        })
        this.movies = res.data?.list || []
      } catch (e) {
        this.$message.error('加载电影列表失败')
      } finally {
        this.loading = false
      }
    },
    goToDetail(id) {
      this.$router.push(`/movie/${id}`)
    },
    resetFilters() {
      this.keyword = ''
      this.genre = ''
      this.region = ''
      this.loadMovies()
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #2d3436;
  margin-bottom: 8px;
}

.page-subtitle {
  color: #636e72;
  font-size: 16px;
}

.filter-section {
  background: white;
  padding: 25px 30px;
  border-radius: 16px;
  margin-bottom: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.search-box {
  flex: 1;
  min-width: 280px;
}

.search-box .el-input {
  width: 100%;
}

.search-box >>> .el-input__inner {
  border-radius: 25px;
  padding-left: 45px;
  height: 45px;
  font-size: 15px;
  border: 2px solid #e9ecef;
  transition: all 0.3s;
}

.search-box >>> .el-input__inner:focus {
  border-color: #667eea;
}

.search-box >>> .el-input__prefix {
  left: 15px;
}

.filter-tags {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-tags .el-select {
  width: 130px;
}

.filter-tags >>> .el-input__inner {
  border-radius: 20px;
  height: 40px;
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 25px;
}

@media (max-width: 1200px) {
  .movie-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 900px) {
  .movie-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 600px) {
  .movie-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
}

.movie-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.movie-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.2);
}

.poster-wrapper {
  position: relative;
  overflow: hidden;
}

.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.9) 0%, rgba(118, 75, 162, 0.9) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.movie-card:hover .card-overlay {
  opacity: 1;
}

.view-detail {
  color: white;
  font-size: 14px;
  font-weight: 600;
  padding: 10px 20px;
  border: 2px solid white;
  border-radius: 25px;
  transition: all 0.3s;
}

.view-detail:hover {
  background: white;
  color: #667eea;
}

.rating-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 4px;
  box-shadow: 0 4px 15px rgba(253, 160, 133, 0.5);
  z-index: 1;
}

.rating-badge i {
  font-size: 14px;
}

.score {
  font-size: 14px;
}

.movie-info {
  padding: 18px;
}

.title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #2d3436;
}

.meta {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.tag {
  font-size: 11px;
  padding: 4px 10px;
  border-radius: 12px;
  font-weight: 500;
}

.genre-tag {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: #2d3436;
}

.region-tag {
  background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%);
  color: #2d3436;
}

.director {
  font-size: 13px;
  color: #636e72;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.director i, .rating-count i {
  margin-right: 5px;
  color: #b2bec3;
}

.rating-count {
  font-size: 12px;
  color: #b2bec3;
}

.empty-tip {
  padding: 80px 20px;
  text-align: center;
  background: white;
  border-radius: 16px;
}
</style>
